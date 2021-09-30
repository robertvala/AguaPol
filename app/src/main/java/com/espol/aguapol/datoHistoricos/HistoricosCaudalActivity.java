package com.espol.aguapol.datoHistoricos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    Button btnExcel;

    //private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historicos_caudal);
        lineChart=findViewById(R.id.lineChartCaudal);
        btnExcel=findViewById(R.id.btnExcelCaudal);
        fechaFin=getIntent().getStringExtra("fechaFin");
        fechaInicio=getIntent().getStringExtra("fechaInicio");
        tramosSeleccionados=getIntent().getStringArrayListExtra("tramos");
        firebaseFirestore=FirebaseFirestore.getInstance();
        lines=new ArrayList<>();
        valores=new HashMap<>();
        date=new ArrayList<>();
        generarData();

        btnExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearExcel();
            }
        });

        //getData();
        //importarCSV();



    }

    private void generarData() {
        String fecha=fechaInicio;
        //Toast.makeText(context, tramosSeleccionados.toString(), Toast.LENGTH_SHORT).show();
        int dias =obtenerDiferenciaDias(fechaInicio,fechaFin);
        Toast.makeText(context, String.valueOf(dias), Toast.LENGTH_SHORT).show();
        for(String tramo:tramosSeleccionados){
            List<Float> promediosPorTramo= new ArrayList<>();
            for(int i=0; i<dias;i++){
                float numeroAlzar= (float) (Math.random()*2+1);
                int numero = (int) (Math.random() * 1);
                float base= 38.0F;
                if(numero>0){
                    base=base+numeroAlzar;
                }
                else{
                    base=base-numeroAlzar;
                }
                promediosPorTramo.add(base);
                date.add(fecha);

                SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date=dateFormat.parse(fecha);
                    Calendar calendar= Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.DATE,1);
                    fecha=dateFormat.format(calendar.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }



            }
            valores.put(tramo,promediosPorTramo);

        }
        initLineChart();


    }

    public void crearExcel(){
        Toast.makeText(context, "Crear excel", Toast.LENGTH_SHORT).show();
        Workbook wb = new HSSFWorkbook();


        Map<String, CellStyle> styles = createStyles(wb);



        Sheet sheet = wb.createSheet("Historico de datos");
        sheet.setFitToPage(true);
        sheet.setHorizontallyCenter(true);


        //title row
        int nRow=0;
        Row titleRow = sheet.createRow(nRow);
        titleRow.setHeightInPoints(20);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("ESCUELA SUPERIOR POLITÉCNICA DEL LITORAL" );
        titleCell.setCellStyle(styles.get("title"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$G$1"));
        nRow=nRow+1;
        titleRow = sheet.createRow(nRow);
        titleRow.setHeightInPoints(20);
        titleCell = titleRow.createCell(0);
        titleCell.setCellValue("CAMPUS PROSPERINA GUSTAVO GALINDO" );
        titleCell.setCellStyle(styles.get("title"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$2:$G$2"));
        nRow=nRow+1;
        titleRow = sheet.createRow(nRow);
        titleRow.setHeightInPoints(20);
        titleCell = titleRow.createCell(0);
        titleCell.setCellValue("CONSUMO AGUA POTABLE");
        titleCell.setCellStyle(styles.get("title"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$3:$G$3"));












        nRow=nRow+1;
        Row row = sheet.createRow(nRow);
        Cell cell = row.createCell(0);
        cell.setCellValue("Fecha inicio: ");
        cell.setCellStyle(styles.get("item_left1"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$4:$B$4"));
        cell = row.createCell(2);
        cell.setCellValue(fechaInicio);
        cell.setCellStyle(styles.get("item_left"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("$C$4:$D$4"));

        nRow=nRow+1;
        row = sheet.createRow(nRow);
        cell = row.createCell(0);
        cell.setCellValue("Fecha fin: ");
        cell.setCellStyle(styles.get("item_left1"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$5:$B$5"));
        cell = row.createCell(2);
        cell.setCellValue(fechaFin);
        cell.setCellStyle(styles.get("item_left"));
        sheet.addMergedRegion(CellRangeAddress.valueOf("$C$5:$F$5"));
        //filas
        nRow=nRow+1;

        nRow=nRow+1;
        int celldatos=0;
        row = sheet.createRow(nRow);
        cell = row.createCell(celldatos);
        cell.setCellValue("Date");
        cell.setCellStyle(styles.get("headerAzul"));
        int i=0;

        celldatos=celldatos+1;

        ArrayList<List<Float>> datos=new ArrayList<>();

        for (Map.Entry<String, List<Float>> entry : valores.entrySet()) {
            cell = row.createCell(celldatos);
            cell.setCellValue(entry.getKey());
            cell.setCellStyle(styles.get("headerAzul"));
            datos.add(entry.getValue());
            celldatos=celldatos+1;
            i=i+1;
        }
        nRow=nRow+1;
        int d=0;

        for(Float dato:datos.get(0)){
            row = sheet.createRow(nRow);
            int celldatos2=0;
            cell = row.createCell(celldatos2);
            cell.setCellValue(date.get(d));
            cell.setCellStyle(styles.get("cell"));
            celldatos2=celldatos2+1;

            for(List<Float> list:datos){
                cell = row.createCell(celldatos2);
                cell.setCellValue(list.get(d));
                cell.setCellStyle(styles.get("cell"));
                celldatos2=celldatos2+1;
            }
            d=d+1;
            nRow=nRow+1;

        }



//crear excel
        String nombreFile="Historico de datos caudal"+".xls";
        File file = new File(context.getExternalFilesDir(null),nombreFile);
        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            wb.write(outputStream);
            Toast.makeText(context.getApplicationContext(),"Reporte generado correctamente",Toast.LENGTH_LONG).show();
            //String[] mailto = {correo};
            Uri uri = FileProvider.getUriForFile(
                    context,
                    "com.espol.aguapol", //(use your app signature + ".provider" )
                    file);

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            String mailto="robavala@espol.edu.ec";
            emailIntent.putExtra(Intent.EXTRA_EMAIL, mailto);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reporte datos históricos de caudal");
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,"Fecha de reporte:  "+fechaInicio+" a "+fechaFin+".\nAtentamente Aguapol.");
            emailIntent.setType("application/xls");
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
            context.startActivity(Intent.createChooser(emailIntent, "Send email using:"));


        } catch (java.io.IOException e) {
            e.printStackTrace();

            Toast.makeText(context.getApplicationContext(),"NO OK",Toast.LENGTH_LONG).show();
            try {
                outputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }



    }

    public static byte[] bitmaptobyte(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 90, baos);
        byte[] b = baos.toByteArray();
        return b;
    }


    private static Map<String, CellStyle> createStyles(Workbook wb){
        Map<String, CellStyle> styles = new HashMap<>();
        CellStyle style;
        Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short)11);
        titleFont.setBold(true);
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(titleFont);
        styles.put("title", style);


        Font headerFont = wb.createFont();
        headerFont.setBold(true);
        style = createBorderedStyle(wb);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(headerFont);

        styles.put("headerAzul", style);

        Font itemFont = wb.createFont();
        itemFont.setFontHeightInPoints((short)12);
        itemFont.setFontName("Trebuchet MS");
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setFont(titleFont);
        style.setFont(itemFont);
        styles.put("item_left", style);

        Font itemresFont = wb.createFont();
        itemresFont.setFontHeightInPoints((short)12);
        itemresFont.setFontName("Trebuchet MS");
        itemresFont.setBold(true);
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setFont(itemresFont);
        styles.put("item_left1", style);



        Font monthFont = wb.createFont();
        monthFont.setFontHeightInPoints((short)12);
        monthFont.setColor(IndexedColors.WHITE.getIndex());
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(monthFont);
        style.setWrapText(true);
        styles.put("header", style);

        Font cellFont = wb.createFont();
        cellFont.setFontHeightInPoints((short)12);
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setFont(cellFont);
        style.setWrapText(true);
        styles.put("cell", style);



        return styles;
    }

    private static CellStyle createBorderedStyle(Workbook wb){
        BorderStyle thin = BorderStyle.THIN;
        short black = IndexedColors.BLACK.getIndex();

        CellStyle style = wb.createCellStyle();
        style.setBorderRight(thin);
        style.setRightBorderColor(black);
        style.setBorderBottom(thin);
        style.setBottomBorderColor(black);
        style.setBorderLeft(thin);
        style.setLeftBorderColor(black);
        style.setBorderTop(thin);
        style.setTopBorderColor(black);
        return style;
    }
    public int obtenerDiferenciaDias(String fechaInicio,String fechaFin){
        SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd");
        Date date1= null;
        try {
            date1 = format.parse(fechaInicio);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2= null;
        try {
            date2 = format.parse(fechaFin);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int dias = (int) ((date2.getTime() - date1.getTime()) / 86400000);
        return  dias;
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
            initLineChart();

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

    private int obtenerNumeroTramo(String i) {
        String[] nombres= getResources().getStringArray(R.array.selectTramos);
        String[] tramosFirestor=getResources().getStringArray(R.array.tramosFireStore);
        ArrayList<String> ltramosFirestore= new ArrayList<>();
        for(String j: tramosFirestor){
            ltramosFirestore.add(j);
        }
        return ltramosFirestore.indexOf(i);

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

    public void importarCSV() {
        String cadena;
        String[] arreglo;
        HashMap<String, HashMap<String, ContorlCaudal>> valoresTramos=new HashMap<>();


        try {
            Toast.makeText(context, context.getExternalFilesDir(null) + "/fakedata.csv", Toast.LENGTH_LONG).show();
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

                        Float valorT1 = Float.parseFloat(arreglo[obtenerNumeroTramo(tramo)+1]);

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

            for(String tramo:tramosSeleccionados){
                HashMap<String,ContorlCaudal> v1=valoresTramos.get(tramo);
                List<Float> v2= new ArrayList<>();
                for(Map.Entry<String,ContorlCaudal> entry:v1.entrySet()){
                    if (!date.contains(entry.getKey())){
                        date.add(entry.getKey());
                    }
                    ContorlCaudal contorlCaudal=entry.getValue();
                    v2.add(contorlCaudal.obtenerPromedio());

                }
                valores.put(tramo,v2);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void initLineChart() {
        Toast.makeText(context, String.valueOf(valores.size()), Toast.LENGTH_SHORT).show();

        for(String i: tramosSeleccionados){
            Line line = new Line(getAxisPoints(valores.get(i))).setColor(Color.parseColor("#FFCD41"));  //The color of the broken line (orange)
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