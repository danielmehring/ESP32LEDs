package de.xyzer.esp32leds;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.FileUtils;
import com.github.mikephil.charting.utils.Utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FadeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FadeFragment extends Fragment {

    TextView status, currentIpAddress, currentlyonTextView, constantTextView,
            brightnessTextView, redTextView, greenTextView, blueTextView, brightnessValueTextView, cycleduration,
            redValueTextView, greenValueTextView, blueValueTextView;
    Button updatebtn, setnonconstant;
    Switch onoffswitch;
    SeekBar seekBarBrightness, seekBarRed, seekBarGreen, seekBarBlue;
    LineChart lineChart;
    EditText editTextCycleduration;
    ArrayList<Entry> valuesGraphRed = new ArrayList<>();
    ArrayList<Entry> valuesGraphGreen = new ArrayList<>();
    ArrayList<Entry> valuesGraphBlue = new ArrayList<>();

    private Context context;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FadeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FadeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FadeFragment newInstance(String param1, String param2) {
        FadeFragment fragment = new FadeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_fade, container, false);
        status = root.findViewById(R.id.status2);
        currentIpAddress = root.findViewById(R.id.currentIpAdress2);
        updatebtn = root.findViewById(R.id.updatebtn2);

        currentlyonTextView = root.findViewById(R.id.currentlyonTextView2);
        constantTextView = root.findViewById(R.id.constantTextView2);
        brightnessTextView = root.findViewById(R.id.brightnessTextView2);
        redTextView = root.findViewById(R.id.redTextView2);
        greenTextView = root.findViewById(R.id.greenTextView2);
        blueTextView = root.findViewById(R.id.blueTextView2);

        cycleduration = root.findViewById(R.id.cycleDurationTextView);

        onoffswitch = root.findViewById(R.id.onoffswitch2);
        brightnessValueTextView = root.findViewById(R.id.brightnessValueTextView2);
        seekBarBrightness = root.findViewById(R.id.seekBarBrightness2);

        editTextCycleduration = root.findViewById(R.id.editTextCycleduration);
        seekBarRed = root.findViewById(R.id.seekBarRed);
        seekBarGreen = root.findViewById(R.id.seekBarGreen);
        seekBarBlue = root.findViewById(R.id.seekBarBlue);
        redValueTextView = root.findViewById(R.id.redValueTextView);
        greenValueTextView = root.findViewById(R.id.greenValueTextView);
        blueValueTextView = root.findViewById(R.id.blueValueTextView);

        setnonconstant = root.findViewById(R.id.setnonconstantbutton);
        lineChart = root.findViewById(R.id.lineChart);

        lineChart.setTouchEnabled(false);
        lineChart.setPinchZoom(false);

        //editTextCycleduration.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextCycleduration.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == 6) {
                    String input = v.getText().toString();
                    if (input.matches("[0-9]+") && input.length() > 2) {
                        startNetwork("n", "n", "n", "n", "n", "n", input, "n", "n", "n");
                    } else {
                        Toast.makeText(getContext(), "Invalid Input", Toast.LENGTH_SHORT).show();
                        startNetwork("n", "n", "n", "n", "n", "n", "n",
                                String.valueOf(MainActivity.pshRed), String.valueOf(MainActivity.pshGreen), String.valueOf(MainActivity.pshBlue));
                    }
                }
                return false;
            }
        });

        setnonconstant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNetwork("n", "0", "n", "n", "n", "n", "n", "n", "n", "n");
            }
        });

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIpAddress.setText(MainActivity.httpAdress);
                if(!MainActivity.httpAdress.matches("")) {
                    startNetwork("n", "n", "n", "n", "n", "n", "n", "n", "n", "n");
                }
            }
        });

        onoffswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                startNetwork(isChecked ? "1" : "0", "n", "n", "n", "n", "n", "n", "n", "n", "n");
            }
        });

        seekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightnessValueTextView.setText(progress == 0 ? "0.01" : String.valueOf((double) progress / 100) + " brightness");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                startNetwork("n", "n", String.valueOf(seekBar.getProgress() == 0 ? 0.01 : (double) seekBar.getProgress() / 100),
                        "n", "n", "n", "n", "n", "n", "n");
            }
        });

        seekBarRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                redValueTextView.setText("phsh red: " + (String.valueOf((int)((double) progress / 100 * MainActivity.cycleduration))));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MainActivity.pshRed = (int)((double) seekBar.getProgress() / 100 * MainActivity.cycleduration);
                startNetwork("n", "n", "n", "n", "n", "n",
                        String.valueOf(MainActivity.cycleduration), String.valueOf(MainActivity.pshRed),
                        String.valueOf(MainActivity.pshGreen), String.valueOf(MainActivity.pshBlue));
            }
        });

        seekBarGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                greenValueTextView.setText("phsh green: " + (String.valueOf((int)((double) progress / 100 * MainActivity.cycleduration))));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MainActivity.pshGreen = (int)((double) seekBar.getProgress() / 100 * MainActivity.cycleduration);
                startNetwork("n", "n", "n", "n", "n", "n",
                        String.valueOf(MainActivity.cycleduration), String.valueOf(MainActivity.pshRed),
                        String.valueOf(MainActivity.pshGreen), String.valueOf(MainActivity.pshBlue));
            }
        });

        seekBarBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                blueValueTextView.setText("phsh blue: " + (String.valueOf((int)((double) progress / 100 * MainActivity.cycleduration))));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MainActivity.pshBlue = (int)((double) seekBar.getProgress() / 100 * MainActivity.cycleduration);
                startNetwork("n", "n", "n", "n", "n", "n",
                        String.valueOf(MainActivity.cycleduration), String.valueOf(MainActivity.pshRed),
                        String.valueOf(MainActivity.pshGreen), String.valueOf(MainActivity.pshBlue));
            }
        });

        if(!MainActivity.httpAdress.matches("")) {
            currentIpAddress.setText(MainActivity.httpAdress);
            startNetwork("n", "n", "n", "n", "n", "n", "n", "n", "n", "n");
        }


        return root;
    }

    public void updateVars(boolean onoff, boolean constant, double brightness, int cycleDuration, int pshRed, int pshGreen, int pshBlue, boolean online) {
        MainActivity.ison = onoff;
        MainActivity.isconstant = constant;
        MainActivity.brightness = brightness;
        MainActivity.pshRed = pshRed;
        MainActivity.pshGreen = pshGreen;
        MainActivity.pshBlue = pshBlue;
        MainActivity.cycleduration = cycleDuration;

        onoffswitch.setChecked(onoff);

        if(online) {
            currentlyonTextView.setText("on/off: " + MainActivity.ison);
            constantTextView.setText("constant/nonconstant: " + MainActivity.isconstant);
            brightnessTextView.setText("brightness: " + MainActivity.brightness);
            redTextView.setText("phaseshift red: " + MainActivity.pshRed);
            greenTextView.setText("phaseshift green: " + MainActivity.pshGreen);
            blueTextView.setText("phaseshift blue: " + MainActivity.pshBlue);
            brightnessValueTextView.setText(String.valueOf(MainActivity.brightness) + " brightness");
            cycleduration.setText("cycle duration: " + MainActivity.cycleduration);
            editTextCycleduration.setText("" + MainActivity.cycleduration);
            seekBarBrightness.setProgress((int) (MainActivity.brightness * 100));

            seekBarRed.setProgress((int)((double) 100 * MainActivity.pshRed / MainActivity.cycleduration));
            redValueTextView.setText("phsh red: " + MainActivity.pshRed);
            seekBarGreen.setProgress((int)((double) 100 * MainActivity.pshGreen / MainActivity.cycleduration));
            greenValueTextView.setText("phsh green: " + MainActivity.pshGreen);
            seekBarBlue.setProgress((int)((double) 100 * MainActivity.pshBlue / MainActivity.cycleduration));
            blueValueTextView.setText("phsh blue: " + MainActivity.pshBlue);

            
        } else {
            currentlyonTextView.setText("on/off: ");
            constantTextView.setText("constant/nonconstant: ");
            brightnessTextView.setText("brightness: ");
            redTextView.setText("phaseshift red: ");
            greenTextView.setText("phaseshift green: ");
            blueTextView.setText("phaseshift blue: ");
            brightnessValueTextView.setText(" brightness");
            cycleduration.setText("cycle duration: ");
            editTextCycleduration.setText("");
            seekBarBrightness.setProgress(100);

            seekBarRed.setProgress(0);
            redValueTextView.setText("phsh red: 0");
            seekBarGreen.setProgress(0);
            greenValueTextView.setText("phsh green: 0");
            seekBarBlue.setProgress(0);
            blueValueTextView.setText("phsh blue: 0");
        }
    }

    public void updateVars(boolean online) {
        if(online) {
            currentlyonTextView.setText("on/off: " + MainActivity.ison);
            constantTextView.setText("constant/nonconstant: " + MainActivity.isconstant);
            brightnessTextView.setText("brightness: " + MainActivity.brightness);
            redTextView.setText("red: " + MainActivity.pshRed);
            greenTextView.setText("green: " + MainActivity.pshGreen);
            blueTextView.setText("blue: " + MainActivity.pshBlue);
            brightnessValueTextView.setText(String.valueOf(MainActivity.brightness) + " brightness");
            cycleduration.setText("cycle duration: " + MainActivity.cycleduration);
            editTextCycleduration.setText("" + MainActivity.cycleduration);
            seekBarBrightness.setProgress((int) (MainActivity.brightness * 100));

            seekBarRed.setProgress((int)((double) 100 * MainActivity.pshRed / MainActivity.cycleduration));
            redValueTextView.setText("phsh red: " + MainActivity.pshRed);
            seekBarGreen.setProgress((int)((double) 100 * MainActivity.pshGreen / MainActivity.cycleduration));
            greenValueTextView.setText("phsh green: " + MainActivity.pshGreen);
            seekBarBlue.setProgress((int)((double) 100 * MainActivity.pshBlue / MainActivity.cycleduration));
            blueValueTextView.setText("phsh blue: " + MainActivity.pshBlue);

            
        } else {
            currentlyonTextView.setText("on/off: ");
            constantTextView.setText("constant/nonconstant: ");
            brightnessTextView.setText("brightness: ");
            redTextView.setText("phaseshift red: ");
            greenTextView.setText("phaseshift green: ");
            blueTextView.setText("phaseshift blue: ");
            brightnessValueTextView.setText(" brightness");
            cycleduration.setText("cycle duration: ");
            editTextCycleduration.setText("");
            seekBarBrightness.setProgress(100);

            seekBarRed.setProgress(0);
            redValueTextView.setText("phsh red: 0");
            seekBarGreen.setProgress(0);
            greenValueTextView.setText("phsh green: 0");
            seekBarBlue.setProgress(0);
            blueValueTextView.setText("phsh blue: 0");
        }
    }

    private LineData generateLineData(int width /* width in px */) {
        /*double temp = MainActivity.cycleduration / 2;
        int x = (int)((double)MainActivity.cycleduration / (double) width);
        for(int i = 0; i < MainActivity.cycleduration; ++x) {
            valuesGraphRed.add(new Entry(i,  (float)511.5 * (float)(1 + Math.sin((Math.PI * i / temp) - (Math.PI * (MainActivity.pshRed / temp)))) ));
            valuesGraphGreen.add(new Entry(i,  (float)511.5 * (float)(1 + Math.sin((Math.PI * i / temp) - (Math.PI * (MainActivity.pshGreen / temp)))) ));
            valuesGraphBlue.add(new Entry(i,  (float)511.5 * (float)(1 + Math.sin((Math.PI * i / temp) - (Math.PI * (MainActivity.pshBlue / temp)))) ));
        }*/

        int x = (int)((double)MainActivity.cycleduration / (double) width);
        for(int i = 0; i < MainActivity.cycleduration; ++x) {
            valuesGraphRed.add(new Entry(i, i));
            valuesGraphGreen.add(new Entry(i, i));
            valuesGraphBlue.add(new Entry(i, i));
        }

        ArrayList<ILineDataSet> sets = new ArrayList<>();

        LineDataSet ds1 = new LineDataSet(valuesGraphRed, "Red");
        LineDataSet ds2 = new LineDataSet(valuesGraphGreen, "Green");
        LineDataSet ds3 = new LineDataSet(valuesGraphBlue, "Blue");

        ds1.setLineWidth(2f);
        ds2.setLineWidth(2f);
        ds3.setLineWidth(2f);

        ds1.setDrawCircles(false);
        ds2.setDrawCircles(false);
        ds3.setDrawCircles(false);


        ds1.setColor(Color.RED);
        ds2.setColor(Color.GREEN);
        ds3.setColor(Color.BLUE);

        // load DataSets from files in assets folder
        sets.add(ds1);
        sets.add(ds2);
        sets.add(ds3);

        LineData d = new LineData(sets);
        return d;
    }

    public void updateGraph() {
        lineChart.setDrawGridBackground(false);

        lineChart.setData(generateLineData(lineChart.getWidth()));
        lineChart.animateX(3000);


        Legend l = lineChart.getLegend();

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMaximum(1023);
        leftAxis.setAxisMinimum(0);

        lineChart.getAxisRight().setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setEnabled(false);
    }


    public void startNetwork(String onoff, String constant, String brightness, String curRed, String curGreen, String curBlue,
                             String cycleduration, String pshR /*in ms*/, String pshG, String pshB) {
        new FadeFragment.networkyAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, onoff, constant, brightness, curRed, curGreen, curBlue,
                cycleduration, pshR, pshG, pshB);
    }

    private class networkyAsyncTask extends AsyncTask<String, String, String> {
        private WeakReference<FadeFragment> activityWeakReference;

        networkyAsyncTask(FadeFragment activity) {
            activityWeakReference = new WeakReference<FadeFragment>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            FadeFragment activity = activityWeakReference.get();
            if (activity == null) {
                return;
            }

        }

        @Override
        protected String doInBackground(String... strings) {


            try {

                Connection.Response get = Jsoup.connect("http://" + MainActivity.httpAdress)
                        .header("body", strings[0] + ";" + strings[1] + ";" + strings[2] + ";" + strings[3] + ";" + strings[4] + ";" + strings[5]
                                + ";" + strings[6] + ";" + strings[7] + ";" + strings[8] + ";" + strings[9] + ";" + MainActivity.password)
                        .method(Connection.Method.GET)
                        .execute();

                return get.body();

            } catch (ConnectException connectException){
                return "noconnection";
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }



        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            FadeFragment activity = activityWeakReference.get();
            if (activity == null) {
                return;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            FadeFragment activity = activityWeakReference.get();
            if (activity == null) {
                return;
            }






            if(s.matches("noconnection")) {
                activity.status.setText("offline");
                activity.status.setTextColor(Color.parseColor("#808080"));
                updateVars(false);
            }

            if(s.contains(";")) {
                Log.println(Log.DEBUG, "DEBUG", s);
                String[] vars = s.split(";");




                boolean pshR0 = vars[7].matches("0");
                boolean pshG0 = vars[8].matches("0");
                boolean pshB0 = vars[9].matches("0");


                activity.status.setText("online");
                activity.status.setTextColor(Color.rgb(115, 255, 115));

                int pshR = 0;
                int pshG = 0;
                int pshB = 0;

                if(!pshR0)
                    pshR = Integer.valueOf(vars[7]);
                if(!pshG0)
                    pshG = Integer.valueOf(vars[8]);
                if(!pshB0)
                    pshB = Integer.valueOf(vars[9]);


                updateVars(vars[0].matches("1") ? true : false, vars[1].matches("1") ? true : false, Double.valueOf(vars[2]),
                        Integer.valueOf(vars[6]), pshR, pshG, pshB, true);

            }

            //Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();

        }
    }
    
}