package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.slider.Slider;

import java.util.ArrayList;

public class ComponentesActivity extends AppCompatActivity {
    static Client socket;
    Modelo modelo;
    boolean logout = false;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_componentes);
        Client.act = this;

        Client.actComponentes = this;
        socket.client.send("getModel");
        try {Thread.sleep(1000);} //Espera per a rebre i establir el model
        catch (InterruptedException e) {e.printStackTrace();}

        //LOGOUT
        TextView btn = findViewById(R.id.btnLogout);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout = true;
                desconectar();
            }
        });

        //GUI FORMING
        makeGUI();
    }

    public void connectionClosedMessage(){ //comprobarConexion
        runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Client.act);
                builder.setTitle("Connection error");
                builder.setMessage("There has been an error with the Server connection, please try again later, we are working on it.\nThank you!");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        startActivity(new Intent(ComponentesActivity.this, LoginActivity.class));
                    }
                });
                builder.show();
            }
        });
    }

    public void desconectar(){
        socket.desconecta();
        startActivity(new Intent(ComponentesActivity.this, LoginActivity.class));
    }

    private void makeGUI(){
        //Agafa la layout on posar els components
        LinearLayout componentLayout = findViewById(R.id.componentsLayout);

        if (modelo != null && modelo.model != null) {

            for (Block block : modelo.model) {//Per cada bloc
                //Crea bloc
                CBlock nBlock = new CBlock(getApplicationContext());
                nBlock.setOrientation(LinearLayout.VERTICAL);
                nBlock.setName(block.getName());

                for (Object object : block) {//Per cada component del bloc
                    switch (object.getClass().toString()) {
                        //Switch
                        case "class com.example.project.SSwitch":
                            SSwitch switchData = (SSwitch) object;
                            CSwitch newSwitch = new CSwitch(this);
                            newSwitch.setId(switchData.getId());
                            newSwitch.setTitle(switchData.getTitle());
                            newSwitch.setChecked(switchData.getValue());
                            newSwitch.setText("Switch:");
                            nBlock.addView(newSwitch);
                            break;
                        //Slider
                        case "class com.example.project.SSlider":
                            SSlider sliderData = (SSlider) object;
                            CSlider newSlider = new CSlider(this);
                            newSlider.setId(sliderData.getId());
                            newSlider.setTitle(sliderData.getTitle());
                            newSlider.setValueFrom(sliderData.getMin()/sliderData.getConversionFactor());
                            newSlider.setValueTo(sliderData.getMax()/sliderData.getConversionFactor());
                            newSlider.setValue(sliderData.getValue()/sliderData.getConversionFactor());
                            newSlider.setStepSize(sliderData.getStep()/sliderData.getConversionFactor());
                            newSlider.setTickVisible(true);
                            newSlider.setConversionFactor(sliderData.getConversionFactor());
                            nBlock.addView(newSlider);
                            break;
                        //Dropdown
                        case "class com.example.project.SDropdown":
                            SDropdown dropdownData = (SDropdown) object;
                            CDropdown newDropdown = new CDropdown(this);
                            newDropdown.setId(dropdownData.getId());
                            newDropdown.setTitle(dropdownData.getTitle());

                            //ADD OPTIONS HERE
                            ArrayList<String> optionValues = new ArrayList<String>();
                            for (SDropdownOption option : dropdownData.getOptions()){
                                optionValues.add(option.getValue());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, optionValues);
                            newDropdown.setAdapter(adapter);
                            //-----

                            newDropdown.setSelection(dropdownData.getValue());
                            nBlock.addView(newDropdown);
                            break;
                        //Sensor
                        case "class com.example.project.SSensor":
                            SSensor sensorData = (SSensor) object;
                            CSensor newSensor = new CSensor(this);
                            newSensor.setId(sensorData.getId());
                            newSensor.setTitle(sensorData.getTitle());
                            newSensor.setUnit(sensorData.getUnit());
                            newSensor.setThresholdLow(sensorData.getThresholdLow());
                            newSensor.setThresholdHigh(sensorData.getThresholdHigh());
                            newSensor.setValue(sensorData.getValue());
                            newSensor.setText(newSensor.getValue() + newSensor.getUnit());
                            nBlock.addView(newSensor);
                            break;
                        //Unknown class
                        default:
                            Log.i("GUI_ERROR", "Unknown component class");
                            break;
                    }
                }
                componentLayout.addView(nBlock);
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(Client.act);
            builder.setTitle("Null model error");
            builder.setMessage("No model loaded on the server\nQuery for model again?");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    socket.client.send("getModel");
                    try {Thread.sleep(1000);}
                    catch (InterruptedException e) {e.printStackTrace();}
                    makeGUI();
                }
            });
            builder.show();
        }
    }

}

class CBlock extends LinearLayout {
    public CBlock(Context context) {super(context);}

    String name;
    public void setName(String name) {this.name = name;}
    public String getName() {return name;}
}

class CSwitch extends Switch {
    public CSwitch(Context context) {super(context);}

    private int id;
    public void setId(int id) {this.id = id;}
    public int getId() {return id;}

    private String title;
    public void setTitle(String title) {this.title = title;}
    public String getTitle() {return title;}

    public String toString() {return "switch="+"id:"+id+",title:"+title+",value:"+isChecked();}
}

class CSlider extends Slider {
    public CSlider(Context context) {super(context);}

    private int id;
    public void setId(int id) {this.id = id;}
    public int getId() {return id;}

    private String title;
    public void setTitle(String title) {this.title = title;}
    public String getTitle() {return title;}

    private int conversionFactor;
    public void setConversionFactor(int conversionFactor) {this.conversionFactor = conversionFactor;}
    public int getConversionFactor() {return conversionFactor;}

    public String toString() {return "slider="+"id:"+id+",title:"+title+",min:"+getValueFrom()+",max:"+getValueTo()+",step:"+getStepSize()+",conversionFactor:"+conversionFactor+",value:"+getValue();}
}

class CDropdown extends androidx.appcompat.widget.AppCompatSpinner {
    public CDropdown(Context context) {super(context);this.setBackgroundColor(Color.WHITE);}

    int id;
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    String title;
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

}

class CDropdownOption {

    int id;
    public void setId(int id){this.id = id;}
    public int getId() {return id;}

    int index;
    public void setIndex(int index){this.index = index;}
    public int getIndex() {return index;}

    String value;
    public void setValue(String value){this.value = value;}
    public String getValue(){return value;}

}

class CSensor extends androidx.appcompat.widget.AppCompatTextView{
    public CSensor(Context context) {super(context);}

    private int id;
    public void setId(int id) {this.id = id;}
    public int getId() {return id;}

    private String title;
    public void setTitle(String title) {this.title = title;}
    public String getTitle() {return title;}

    private String unit;
    public void setUnit(String unit) {this.unit = unit;}
    public String getUnit() {return unit;}

    private int thresholdLow;
    public void setThresholdLow(int thresholdLow) {this.thresholdLow = thresholdLow;}
    public int getThresholdLow() {return thresholdLow;}

    private int thresholdHigh;
    public void setThresholdHigh(int thresholdHigh) {this.thresholdHigh = thresholdHigh;}
    public int getThresholdHigh() {return thresholdHigh;}

    private int value;
    public void setValue(int value) {this.value = value;}
    public int getValue() {return value;}

    public String toString() {
        return "sensor=" + "id:"+id+",title:"+title+",unit:"+unit+",low:"+thresholdLow+",high:"+thresholdHigh+",value:"+value;
    }
}
