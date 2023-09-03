package de.xyzer.esp32leds;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ColorsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ColorsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView status, currentIpAddress, currentlyonTextView, constantTextView,
            brightnessTextView, redTextView, greenTextView, blueTextView, brightnessValueTextView,
            textViewAlarmDay, textViewAlarmHour, textViewAlarmMinute;
    Button updatebtn, colorchoosebutton, setconstant, applyred, applygreen, applyblue, selectDateTime;
    Switch onoffswitch, onoffswitchalarm;
    SeekBar seekBarBrightness;
    EditText editTextRed, editTextGreen, editTextBlue;

    ConstraintLayout mLayout;
    int mDefaultColor;

    public ColorsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ColorsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ColorsFragment newInstance(String param1, String param2) {
        ColorsFragment fragment = new ColorsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_colors, container, false);
        status = root.findViewById(R.id.status);
        currentIpAddress = root.findViewById(R.id.currentIpAdress);
        updatebtn = root.findViewById(R.id.updatebtn);

        currentlyonTextView = root.findViewById(R.id.currentlyonTextView);
        constantTextView = root.findViewById(R.id.constantTextView);
        brightnessTextView = root.findViewById(R.id.brightnessTextView);
        redTextView = root.findViewById(R.id.redTextView);
        greenTextView = root.findViewById(R.id.greenTextView);
        blueTextView = root.findViewById(R.id.blueTextView);

        onoffswitch = root.findViewById(R.id.onoffswitch);
        brightnessValueTextView = root.findViewById(R.id.brightnessValueTextView);
        seekBarBrightness = root.findViewById(R.id.seekBarBrightness);

        onoffswitchalarm = root.findViewById(R.id.onoffswitchalarm);

        mLayout = root.findViewById(R.id.layoutConstraint);
        mDefaultColor = Color.rgb(255, 255, 255);
        colorchoosebutton = root.findViewById(R.id.colorchoosebutton);

        setconstant = root.findViewById(R.id.setconstantbutton);

        editTextRed = root.findViewById(R.id.editTextRed);
        editTextGreen = root.findViewById(R.id.editTextGreen);
        editTextBlue = root.findViewById(R.id.editTextBlue);
        applyred = root.findViewById(R.id.applyredbutton);
        applygreen = root.findViewById(R.id.applygreenbutton);
        applyblue = root.findViewById(R.id.applybluebutton);

        textViewAlarmDay = root.findViewById(R.id.textViewAlarmDay);
        textViewAlarmHour = root.findViewById(R.id.textViewAlarmHour);
        textViewAlarmMinute = root.findViewById(R.id.textViewAlarmMinute);

        selectDateTime = root.findViewById(R.id.buttonDateTime);

        selectDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogTimeDate();
            }
        });

        applyred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextRed.getText().toString().matches("[0-9]+")) {
                    int i = Integer.parseInt(editTextRed.getText().toString());
                    if(i <= 1023) {
                        startNetwork("n", "n", "n", "" + i, "n", "n", "n", "n", "n", "n");
                    } else {
                        Toast.makeText(getContext(), "10 bit values have to be between 0 and 1023", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        applygreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextGreen.getText().toString().matches("[0-9]+")) {
                    int i = Integer.parseInt(editTextGreen.getText().toString());
                    if(i <= 1023) {
                        startNetwork("n", "n", "n", "n", "" + i, "n", "n", "n", "n", "n");
                    } else {
                        Toast.makeText(getContext(), "10 bit values have to be between 0 and 1023", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        applyblue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextBlue.getText().toString().matches("[0-9]+")) {
                    int i = Integer.parseInt(editTextBlue.getText().toString());
                    if(i <= 1023) {
                        startNetwork("n", "n", "n", "n", "n", "" + i, "n", "n", "n", "n");
                    } else {
                        Toast.makeText(getContext(), "10 bit values have to be between 0 and 1023", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        setconstant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNetwork("n", "1", "n", "n", "n", "n", "n", "n", "n", "n");
            }
        });

        colorchoosebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

        onoffswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNetwork(onoffswitch.isChecked() ? "1" : "0", "n", "n", "n", "n", "n", "n", "n", "n", "n");
            }
        });



        onoffswitchalarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNetwork(onoffswitchalarm.isChecked() ? "1" : "0", "n", "n", "n");
            }
        });

        seekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightnessValueTextView.setText(progress == 0 ? "0.01" : String.valueOf((double) progress / 100));
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

        if(!MainActivity.httpAdress.matches("")) {
            currentIpAddress.setText(MainActivity.httpAdress);
            startNetwork("n", "n", "n", "n", "n", "n", "n", "n", "n", "n");
        }

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIpAddress.setText(MainActivity.httpAdress);
                if(!MainActivity.httpAdress.matches("")) {
                    startNetwork("n", "n", "n", "n", "n", "n", "n", "n", "n", "n");
                }
            }
        });

        return root;
    }

    private void showDialogTimeDate() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                        if (Calendar.MONDAY == dayOfWeek) {
                            dayOfWeek = 1;
                        } else if (Calendar.TUESDAY == dayOfWeek) {
                            dayOfWeek = 2;
                        } else if (Calendar.WEDNESDAY == dayOfWeek) {
                            dayOfWeek = 3;
                        } else if (Calendar.THURSDAY == dayOfWeek) {
                            dayOfWeek = 4;
                        } else if (Calendar.FRIDAY == dayOfWeek) {
                            dayOfWeek = 5;
                        } else if (Calendar.SATURDAY == dayOfWeek) {
                            dayOfWeek = 6;
                        } else if (Calendar.SUNDAY == dayOfWeek) {
                            dayOfWeek = 7;
                        }

                        startNetwork("n", "" + dayOfWeek, "" + calendar.getTime().getHours(), "" + calendar.getTime().getMinutes());
                    }
                };

                new TimePickerDialog(getContext(), timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        };
        new DatePickerDialog(getContext(), dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void updateVars(boolean onoff, boolean constant, double brightness, int curRed, int curGreen, int curBlue, boolean online,
                           boolean onoffalarm, int alarmday, int alarmhour, int alarmminute) {
        MainActivity.ison = onoff;
        MainActivity.isconstant = constant;
        MainActivity.brightness = brightness;
        MainActivity.curRed = curRed;
        MainActivity.curGreen = curGreen;
        MainActivity.curBlue = curBlue;

        mDefaultColor = Color.rgb(tenToEightBit(curRed), tenToEightBit(curGreen), tenToEightBit(curBlue));

        onoffswitch.setChecked(onoff);

        if(online) {
            currentlyonTextView.setText("on/off: " + MainActivity.ison);
            constantTextView.setText("constant/nonconstant: " + MainActivity.isconstant);
            brightnessTextView.setText("brightness: " + MainActivity.brightness);
            redTextView.setText("red: " + MainActivity.curRed);
            greenTextView.setText("green: " + MainActivity.curGreen);
            blueTextView.setText("blue: " + MainActivity.curBlue);
            brightnessValueTextView.setText(String.valueOf(MainActivity.brightness));
            seekBarBrightness.setProgress((int) (MainActivity.brightness * 100));

            editTextRed.setText("" + MainActivity.curRed);
            editTextGreen.setText("" + MainActivity.curGreen);
            editTextBlue.setText("" + MainActivity.curBlue);

            onoffswitchalarm.setChecked(onoffalarm);

            String day = "";
            switch (alarmday) {
                case 0:
                    day = "Unset";
                    break;
                case 1:
                    day = "Monday";
                    break;
                case 2:
                    day = "Tuesday";
                    break;
                case 3:
                    day = "Wednesday";
                    break;
                case 4:
                    day = "Thursday";
                    break;
                case 5:
                    day = "Friday";
                    break;
                case 6:
                    day = "Saturday";
                    break;
                case 7:
                    day = "Sunday";
                    break;

            }
            textViewAlarmDay.setText(day);
            textViewAlarmHour.setText("" + alarmhour);
            if (alarmminute < 10)
                textViewAlarmMinute.setText("0" + alarmminute);
            else
                textViewAlarmMinute.setText("" + alarmminute);
        } else {
            currentlyonTextView.setText("on/off: ");
            constantTextView.setText("constant/nonconstant: ");
            brightnessTextView.setText("brightness: ");
            redTextView.setText("red: ");
            greenTextView.setText("green: ");
            blueTextView.setText("blue: ");
            brightnessValueTextView.setText("");
            seekBarBrightness.setProgress(100);

            editTextRed.setText("");
            editTextGreen.setText("");
            editTextBlue.setText("");

            onoffswitchalarm.setChecked(false);

            textViewAlarmDay.setText("Monday");
            textViewAlarmHour.setText("00");
            textViewAlarmMinute.setText("00");
        }
    }

    public void updateVars(boolean online) {
        if(online) {
            currentlyonTextView.setText("on/off: " + MainActivity.ison);
            constantTextView.setText("constant/nonconstant: " + MainActivity.isconstant);
            brightnessTextView.setText("brightness: " + MainActivity.brightness);
            redTextView.setText("red: " + MainActivity.curRed);
            greenTextView.setText("green: " + MainActivity.curGreen);
            blueTextView.setText("blue: " + MainActivity.curBlue);
            brightnessValueTextView.setText(String.valueOf(MainActivity.brightness));
            seekBarBrightness.setProgress((int) (MainActivity.brightness * 100));

            editTextRed.setText("" + MainActivity.curRed);
            editTextGreen.setText("" + MainActivity.curGreen);
            editTextBlue.setText("" + MainActivity.curBlue);
        } else {
            currentlyonTextView.setText("on/off: ");
            constantTextView.setText("constant/nonconstant: ");
            brightnessTextView.setText("brightness: ");
            redTextView.setText("red: ");
            greenTextView.setText("green: ");
            blueTextView.setText("blue: ");
            brightnessValueTextView.setText("");
            seekBarBrightness.setProgress(100);

            editTextRed.setText("");
            editTextGreen.setText("");
            editTextBlue.setText("");

            onoffswitchalarm.setChecked(false);

            textViewAlarmDay.setText("Monday");
            textViewAlarmHour.setText("00");
            textViewAlarmMinute.setText("00");
        }
    }

    public void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(getContext(), mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mDefaultColor = color;
                startNetwork("n", "n", "n", String.valueOf(eightToTenBit(Color.red(color))), String.valueOf(eightToTenBit(Color.green(color))),
                        String.valueOf(eightToTenBit(Color.blue(color))), "n", "n", "n", "n");
            }
        });
        colorPicker.show();
    }

    public int tenToEightBit(int tenbit) {
        return (int) (tenbit * 255 / 1023);
    }

    public int eightToTenBit(int eightbit) {
        return (int) (eightbit * 1023 / 255);
    }

    public void startNetwork(String onoff, String constant, String brightness, String curRed, String curGreen, String curBlue,
                             String cycleduration, String pshR /*in ms*/, String pshG, String pshB) {
        new networkyAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, onoff, constant, brightness, curRed, curGreen, curBlue,
                cycleduration, pshR, pshG, pshB);
    }

    public void startNetwork(String onoff, String alarmday, String alarmhour, String alarmminute) {
        new networkyAsyncTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "", "", "", "", "", "",
                "", "", "", "", onoff, alarmday, alarmhour, alarmminute);
    }

    private class networkyAsyncTask extends AsyncTask<String, String, String> {
        private WeakReference<ColorsFragment> activityWeakReference;

        networkyAsyncTask(ColorsFragment activity) {
            activityWeakReference = new WeakReference<ColorsFragment>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ColorsFragment activity = activityWeakReference.get();
            if (activity == null) {
                return;
            }

        }

        @Override
        protected String doInBackground(String... strings) {


            try {
                if(strings.length == 10) {
                    Connection.Response get = Jsoup.connect("http://" + MainActivity.httpAdress)
                            .header("body", strings[0] + ";" + strings[1] + ";" + strings[2] + ";" + strings[3] + ";" + strings[4] + ";" + strings[5]
                                    + ";" + strings[6] + ";" + strings[7] + ";" + strings[8] + ";" + strings[9] + ";" + MainActivity.password)
                            .method(Connection.Method.GET)
                            .execute();

                    return get.body();
                } else {
                    Log.println(Log.DEBUG, "LOG", strings[10] + ";" + strings[11] + ";" + strings[12] + ";" + strings[13] + ";" + MainActivity.password);
                    Connection.Response get = Jsoup.connect("http://" + MainActivity.httpAdress)
                            .header("alarm", strings[10] + ";" + strings[11] + ";" + strings[12] + ";" + strings[13] + ";" + MainActivity.password)
                            .method(Connection.Method.GET)
                            .execute();

                    return get.body();
                }

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

            ColorsFragment activity = activityWeakReference.get();
            if (activity == null) {
                return;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            ColorsFragment activity = activityWeakReference.get();
            if (activity == null) {
                return;
            }



            if(s.matches("noconnection")) {
                activity.status.setText("offline");
                activity.status.setTextColor(Color.parseColor("#808080"));
                updateVars(false);
            }

            if(s.contains(";")) {
                String temp = s;
                int countofsemicolons = temp.length() - temp.replaceAll(";", "").length();

                Log.println(Log.DEBUG, "RESPONSE", s);

                if (countofsemicolons == 14) {
                    String[] vars = s.split(";");


                    activity.status.setText("online");
                    activity.status.setTextColor(Color.rgb(115, 255, 115));
                    updateVars(vars[0].matches("1") ? true : false, vars[1].matches("1") ? true : false, Double.valueOf(vars[2]),
                            Integer.valueOf(vars[3]), Integer.valueOf(vars[4]), Integer.valueOf(vars[5]), true, vars[10].matches("1") ? true : false,
                            Integer.valueOf(vars[11]), Integer.valueOf(vars[12]), Integer.valueOf(vars[13]));
                } else {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }


        }
    }
}