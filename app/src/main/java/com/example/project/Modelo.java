package com.example.project;
import android.content.Context;
import android.util.Log;
import android.widget.Switch;
import com.google.android.material.slider.Slider;
import java.util.ArrayList;

public class Modelo {

    public Modelo(String modelString){
        parseModel(modelString);
    }

    public void parseModel(String modelString){
        ArrayList<Block> model = new ArrayList<Block>();
        String[] blocks = modelString.split("#");
        for (String block : blocks){
            if (block.isEmpty()){break;}
            //CREACIO BLOCK
            String blockParams = block.split(";")[0];
            Block newBlock = new Block();
            newBlock.setName(block.split("=")[1].split(":")[1]);
            //CREACIO COMPONENTS
            String[] blockComponentsList = block.split(";")[1].split("!");
            for (String component : blockComponentsList){
                if (component.isEmpty()){break;}
                String componentType = component.split("=")[0];
                String[] componentParams = component.split("=")[1].split(",");
                switch (componentType){
                    case "switch":
                        CSwitch newSwitch = new CSwitch(null);
                        for (String param : componentParams){
                            if (param.isEmpty()){break;}
                            String pName = param.split(":")[0];
                            String pValue = param.split(":")[1];
                            switch (pName){
                                case "id":
                                    newSwitch.setId(Integer.parseInt(pValue));
                                    break;
                                case "title":
                                    newSwitch.setTitle(pValue);
                                    break;
                                case "value":
                                    if (pValue.contentEquals("true")){
                                        newSwitch.setSelected(true);
                                    } else {
                                        newSwitch.setSelected(false);
                                    }
                                    break;
                                default:
                                    Log.i("PARSER_ERROR","Unknown parameter "+ param + "for component type" + componentType);
                                    break;
                            }
                        }
                        newBlock.add(newSwitch);
                        break;
                    case "slider":
                        CSlider newSlider = new CSlider(null);
                        for (String param : componentParams){
                            if (param.isEmpty()){break;}
                            String pName = param.split(":")[0];
                            String pValue = param.split(":")[1];
                            switch (pName){
                                case "id":
                                    newSlider.setId(Integer.parseInt(pValue));
                                    break;
                                case "title":
                                    newSlider.setTitle(pValue);
                                    break;
                                case "min":
                                    newSlider.setValueFrom(Integer.parseInt(pValue));
                                    break;
                                case "max":
                                    newSlider.setValueTo(Integer.parseInt(pValue));
                                    break;
                                case "step":
                                    newSlider.setStepSize(Integer.parseInt(pValue));
                                    break;
                                case "conversionFactor":
                                    newSlider.setConversionFactor(Integer.parseInt(pValue));
                                    break;
                                case "value":
                                    newSlider.setValue(Integer.parseInt(pValue));
                                    break;
                                default:
                                    Log.i("PARSER_ERROR","Unknown parameter "+ param + "for component type" + componentType);
                                    break;
                            }
                        }
                        newBlock.add(newSlider);
                        break;
                    case "dropdown":
                        CDropdown newDropdown = new CDropdown(null);
                        for (String param : componentParams) {
                            if (param.isEmpty()){break;}
                            String pName = param.split(":")[0];
                            String pValue = param.split(":")[1];
                            switch (pName) {
                                case "id":
                                    newDropdown.setId(Integer.parseInt(pValue));
                                    break;
                                case "title":
                                    newDropdown.setTitle(pValue);
                                    break;
                                default:
                                    Log.i("PARSER_ERROR","Unknown parameter "+ param + "for component type" + componentType);
                                    break;
                            }
                        }
                        newBlock.add(newDropdown);
                        break;
                    case "sensor":
                        CSensor newSensor = new CSensor(null);
                        for (String param : componentParams) {
                            if (param.isEmpty()){break;}
                            String pName = param.split(":")[0];
                            String pValue = param.split(":")[1];
                            switch (pName) {
                                case "id":
                                    newSensor.setId(Integer.parseInt(pValue));
                                    break;
                                case "title":
                                    newSensor.setTitle(pValue);
                                    break;
                                case "unit":
                                    newSensor.setUnit(pValue);
                                    break;
                                case "low":
                                    newSensor.setThresholdLow(Integer.parseInt(pValue));
                                    break;
                                case "high":
                                    newSensor.setThresholdHigh(Integer.parseInt(pValue));
                                    break;
                                case "value":
                                    newSensor.setValue(Integer.parseInt(pValue));
                                default:
                                    Log.i("PARSER_ERROR","Unknown parameter "+ param + "for component type" + componentType);
                                    break;
                            }
                        }
                        newBlock.add(newSensor);
                        break;

                    default:
                        Log.i("PARSER_ERROR","Unknown_component_type:"+componentType);
                        break;
                }
            }
            model.add(newBlock);
        }
        ComponentesActivity.modelo = model;
    }
}


class Block extends ArrayList<Object>{
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

    public String toString() {return "switch="+"id:"+id+",title:"+getTitle()+",value:"+isSelected();}

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected == true){
            setText("ON");
        } else {
            setText("OFF");
        }
    }
}

class CSlider extends Slider{
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
    public CDropdown(Context context) {super(context);}

    int id;
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    String title;
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
}

class CSensor extends androidx.appcompat.widget.AppCompatTextView {
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
