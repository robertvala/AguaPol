package com.espol.aguapol.datoHistoricos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.espol.aguapol.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    List<Float> score;
    List<String> date;
    List<Line> lines;

    //private List<PointValue> mPointValues = new ArrayList<PointValue>();
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
        getData();



    }

    private void getData() {
        valores=new HashMap<>();
        for(String i : tramosSeleccionados){
            date = new ArrayList<>();
            score= new ArrayList<>();
            CollectionReference refTramo=firebaseFirestore.collection(i);
            refTramo.whereGreaterThanOrEqualTo("date",fechaInicio).whereLessThanOrEqualTo("date",fechaFin).orderBy("date", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot snap: queryDocumentSnapshots){
                        float valor=Float.parseFloat(snap.getData().get("promedio").toString());
                        String fecha=snap.getData().get("date").toString();
                        score.add(valor);
                        date.add(fecha);
                        valores.put(obtenerNombreTramo(i),score);
                        Toast.makeText(context, String.valueOf(valores.size()), Toast.LENGTH_SHORT).show();
                    }



                }

            });

            getAxisXLables(date);
            //initLineChart();

        }




    }

    private String obtenerNombreTramo(String i) {
        String[] nombres= getResources().getStringArray(R.array.selectTramos);
        String[] tramosFirestor=getResources().getStringArray(R.array.tramosFireStore);
        ArrayList<String> ltramosFirestore= new ArrayList<>();
        for(String j: tramosFirestor){
            ltramosFirestore.add(j);
        }
        return nombres[ltramosFirestore.indexOf(i)];

    }

    public void getAxisXLables(List<String> date) {
        int cont=0;
        for (String i: date) {
            mAxisXValues.add(new AxisValue(cont).setLabel(i));
            cont++;
        }
    }

    /**
     * Display of each point in the chart
     */
    private List<PointValue> getAxisPoints(List<Float> score) {
        List<PointValue> mPointValues= new ArrayList<>();
        int cont=0;
        for (float i:score) {
            mPointValues.add(new PointValue(cont, i));
            cont++;
        }
        return mPointValues;
    }

    private void initLineChart() {
        Toast.makeText(context, String.valueOf(valores.size()), Toast.LENGTH_SHORT).show();
        
        for(String i: tramosSeleccionados){
            Line line = new Line(getAxisPoints(valores.get(obtenerNombreTramo(i)))).setColor(Color.parseColor("#FFCD41"));  //The color of the broken line (orange)
            line.setShape(ValueShape.CIRCLE);//The shape of each data point on a broken line chart is circular here (there are three kinds: ValueShape. SQUARE ValueShape. CIRCLE ValueShape. DIAMOND)
            line.setCubic(false);//Whether the curve is smooth, that is, whether it is a curve or a broken line
            line.setFilled(false);//Whether or not to fill the area of the curve
            line.setHasLabels(true);//Whether to add notes to the data coordinates of curves
//      Line. setHasLabels OnlyForSelected (true); // Click on the data coordinates to prompt the data (set this line.setHasLabels(true); invalid)
            line.setHasLines(true);//Whether to display with line or not. If it is false, there is no curve but point display
            line.setHasPoints(true);//Whether to display a dot if it is false, there is no origin but only a dot (each data point is a large dot)
            lines.add(line);
        }
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