package com.espol.aguapol.datoHistoricos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.espol.aguapol.Modelo.ContorlCaudal;
import com.espol.aguapol.Modelo.Herramientas;
import com.espol.aguapol.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class HistoricosCaudalActivity extends AppCompatActivity {
    LineChartView lineChart;
    Context context=this;
    String fechaInicio;
    String fechaFin;
    List<String> tramosSeleccionados;
    FirebaseFirestore firebaseFirestore;
    HashMap<String,List<Float>> valores;
    List<String> date = new ArrayList<>();
    List<Float> score= new ArrayList<>();
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historicos_caudal);
        lineChart=findViewById(R.id.lineChartCaudal);
        fechaFin=getIntent().getStringExtra("fechaFin");
        fechaInicio=getIntent().getStringExtra("fechaInicio");
        tramosSeleccionados=getIntent().getStringArrayListExtra("tramos");
        firebaseFirestore=FirebaseFirestore.getInstance();
        Toast.makeText(context, fechaInicio, Toast.LENGTH_SHORT).show();
        Toast.makeText(context, fechaFin, Toast.LENGTH_SHORT).show();
        Toast.makeText(context, tramosSeleccionados.toString(), Toast.LENGTH_SHORT).show();
        //getData();
        importarCSV();



    }

    private void getData() {
        for(String i : tramosSeleccionados){
            CollectionReference refTramo=firebaseFirestore.collection(i);
            refTramo.whereGreaterThanOrEqualTo("date",fechaInicio).whereLessThanOrEqualTo("date",fechaFin).orderBy("date", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot snap: queryDocumentSnapshots){
                        float valor=Float.parseFloat(snap.getData().get("promedio").toString());
                        String fecha=snap.getData().get("date").toString();
                        score.add(valor);
                        date.add(fecha);
                    }


                }

            });
            getAxisXLables();
            getAxisPoints();
            initLineChart();

        }



    }

    public void getAxisXLables() {
        int cont=0;
        for (String i: date) {
            mAxisXValues.add(new AxisValue(cont).setLabel(i));
            cont++;
        }
    }

    /**
     * Display of each point in the chart
     */
    private void getAxisPoints() {
        int cont=0;
        for (float i:score) {
            mPointValues.add(new PointValue(cont, i));
            cont++;
        }
    }

    public void importarCSV() {
        String cadena;
        String[] arreglo;
        HashMap<String, HashMap<String, ContorlCaudal>> valoresTramos=new HashMap<>();


        try {
            FileReader fileReader = new FileReader(context.getExternalFilesDir(null) + "/fakedata.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((cadena = bufferedReader.readLine()) != null) {

                arreglo = cadena.split(",");
                String fechaYhora = arreglo[10];
                String[] arreglo2 = fechaYhora.split(" ");
                String fecha = arreglo2[0];
                String hora = arreglo2[1];
                Herramientas herramientas = new Herramientas();

                Date date = herramientas.obtenerFecha("yyyy-MM-dd", fecha);
                Date dateIncio = herramientas.obtenerFecha("yyyy-MM-dd", fechaInicio);
                Date dateFin = herramientas.obtenerFecha("yyyy-MM-dd", fechaFin);
                long difInicio = herramientas.diferenciaFechas(dateIncio, date);
                long difFin = herramientas.diferenciaFechas(date, dateFin);
                if (difInicio >= 0 && difFin <= 0) {

                    for (String tramo : tramosSeleccionados) {

                        Float valorT1 = Float.parseFloat(tramo);

                        if (!valoresTramos.containsKey(tramo)) {
                            HashMap<String, ContorlCaudal> valoresporTramos = new HashMap<>();
                            HashMap<String, Float> valores = new HashMap<>();
                            valores.put(hora, valorT1);
                            ContorlCaudal contorlCaudal = new ContorlCaudal(fecha, 0.0F, valores);
                            valoresporTramos.put(fecha, contorlCaudal);
                            valoresTramos.put(tramo, valoresporTramos);

                        } else {
                            HashMap<String, ContorlCaudal> v1 = valoresTramos.get(tramo);
                            if (v1.containsKey(fecha)) {
                                ContorlCaudal c1 = v1.get(fecha);
                                HashMap<String, Float> valores = c1.getValores();
                                valores.put(hora, valorT1);
                                c1.setValores(valores);
                                v1.put(fecha, c1);
                                valoresTramos.put(tramo, v1);
                            } else {
                                HashMap<String, Float> valores = new HashMap<>();
                                valores.put(hora, valorT1);
                                ContorlCaudal contorlCaudal = new ContorlCaudal(fecha, 0.0F, valores);
                                v1.put(fecha, contorlCaudal);
                                valoresTramos.put(tramo, v1);
                            }

                        }


                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void initLineChart() {
        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));  //The color of the broken line (orange)
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//The shape of each data point on a broken line chart is circular here (there are three kinds: ValueShape. SQUARE ValueShape. CIRCLE ValueShape. DIAMOND)
        line.setCubic(false);//Whether the curve is smooth, that is, whether it is a curve or a broken line
        line.setFilled(false);//Whether or not to fill the area of the curve
        line.setHasLabels(true);//Whether to add notes to the data coordinates of curves
//      Line. setHasLabels OnlyForSelected (true); // Click on the data coordinates to prompt the data (set this line.setHasLabels(true); invalid)
        line.setHasLines(true);//Whether to display with line or not. If it is false, there is no curve but point display
        line.setHasPoints(true);//Whether to display a dot if it is false, there is no origin but only a dot (each data point is a large dot)
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //Axis of coordinates
        Axis axisX = new Axis(); //X axis
        axisX.setHasTiltedLabels(true);  //Is the font on the X axis oblique or straight, and true oblique?
        axisX.setTextColor(Color.GRAY);  //Setting font color
        //axisX.setName("date"); // table name
        axisX.setTextSize(6);//Set font size
        axisX.setMaxLabelChars(8); //Up to a few X-axis coordinates, which means that your scaling allows the number of data on the X-axis to be 7<=x<=mAxisXValues.length.
        axisX.setValues(mAxisXValues);  //Fill in the coordinate name of the X-axis
        data.setAxisXBottom(axisX); //The x-axis is at the bottom.
        //Data. setAxisXTop (axisX); //x axis at top
        axisX.setHasLines(true); //x-axis dividing line

        // The Y-axis automatically sets the Y-axis limit according to the size of the data (below I will give a solution for fixing the number of Y-axis data)
        Axis axisY = new Axis();  //Y axis
        axisY.setName("");//y axis annotation
        axisY.setTextSize(10);//Set font size
        axisY.setHasLines(true);

        data.setAxisYLeft(axisY);  //The Y-axis is set on the left
        //Data. setAxisYRight (axisY); the // Y axis is set to the right


        //Setting behavioral properties to support zooming, sliding, and Translation
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float) 20);//Maximum method ratio
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);
        /**Note: The following 7, 10 just represent a number to analogize.
         * At that time, it was to solve the fixed number of X-axis data. See (http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2);
         */
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 4;
        v.right = 7;
        lineChart.setCurrentViewport(v);
    }
}