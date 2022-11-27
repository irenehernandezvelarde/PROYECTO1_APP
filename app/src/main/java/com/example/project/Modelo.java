package com.example.project;
import android.content.Context;
import android.util.Log;
import android.widget.Switch;
import com.google.android.material.slider.Slider;
import java.util.ArrayList;

public class Modelo {
    public static ArrayList<Block> model;

    public Modelo(String modelString){
        parseModel(modelString);
    }

    public void parseModel(String modelString){
        model = new ArrayList<Block>();
        String[] blocks = modelString.split("#");
        for (String block : blocks){
            if (block.isEmpty()){break;}
            //CREACIO BLOCK
            String blockParams = block.split(";")[0];
            Block newBlock = new Block();
            newBlock.setName(blockParams.split("=")[1].split(":")[1]);
            //CREACIO COMPONENTS
            String[] blockComponentsList = block.split(";")[1].split("!");
            for (String component : blockComponentsList){
                if (component.isEmpty()){break;}
                String componentType = component.split("=")[0];
                String[] componentParams = component.split("=")[1].split(",");
                switch (componentType){
                    case "switch":
                        SSwitch newSwitch = new SSwitch();
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
                                        newSwitch.setValue(true);
                                    } else {
                                        newSwitch.setValue(false);
                                    }
                                    break;
                                default:
                                    Log.i("PARSER_ERROR","Unknown parameter ("+ pName + ") for component type " + componentType);
                                    break;
                            }
                        }
                        newBlock.add(newSwitch);
                        break;
                    case "slider":
                        SSlider newSlider = new SSlider();
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
                                    newSlider.setMin(Integer.parseInt(pValue));
                                    break;
                                case "max":
                                    newSlider.setMax(Integer.parseInt(pValue));
                                    break;
                                case "step":
                                    newSlider.setStep(Integer.parseInt(pValue));
                                    break;
                                case "conversionFactor":
                                    newSlider.setConversionFactor(Integer.parseInt(pValue));
                                    break;
                                case "value":
                                    newSlider.setValue(Integer.parseInt(pValue));
                                    break;
                                default:
                                    Log.i("PARSER_ERROR","Unknown parameter ("+ pName + ") for component type " + componentType);
                                    break;
                            }
                        }
                        newBlock.add(newSlider);
                        break;
                    case "dropdown":
                        SDropdown newDropdown = new SDropdown();
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
                                case "options": //UNIMPLEMENTED
                                    String[] options = pValue.split("I");
                                    for (String option : options){
                                        if (option.isEmpty()){break;}
                                        SDropdownOption newOption = new SDropdownOption();
                                        String[] optionParams = option.split("-");
                                        for (String optionParam : optionParams){
                                            String opPaName = optionParam.split("_")[0];
                                            String opPaValue = optionParam.split("_")[1];
                                            switch (opPaName){
                                                case "index":
                                                    newOption.setIndex(Integer.valueOf(opPaValue));
                                                    break;
                                                case "id":
                                                    newOption.setId(Integer.valueOf(opPaValue));
                                                    break;
                                                case "value":
                                                    newOption.setValue(opPaValue);
                                                    break;
                                                default:
                                                    Log.i("PARSER_ERROR","Unknown parameter for dropdown option ("+opPaName+")");
                                                    break;
                                            }
                                        }
                                        newDropdown.options.add(newOption);
                                    }
                                    break;
                                case "value": //UNIMPLEMENTED
                                    newDropdown.setValue(Integer.parseInt(pValue));
                                    break;
                                default:
                                    Log.i("PARSER_ERROR","Unknown parameter ("+ pName + ") for component type " + componentType);
                                    break;
                            }
                        }
                        newBlock.add(newDropdown);
                        break;
                    case "sensor":
                        SSensor newSensor = new SSensor();
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
                                    break;
                                default:
                                    Log.i("PARSER_ERROR","Unknown parameter ("+ pName + ") for component type " + componentType);
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
    }
}


class Block extends ArrayList<Object>{
    String name;
    public void setName(String name) {this.name = name;}
    public String getName() {return name;}
}

class SSwitch {

    private int id;
    public void setId(int id) {this.id = id;}
    public int getId() {return id;}

    private String title;
    public void setTitle(String title) {this.title = title;}
    public String getTitle() {return title;}

    private boolean value;
    public void setValue(Boolean value) {this.value = value;}
    public Boolean getValue() {return value;}

    public String toString() {return "switch="+"id:"+id+",title:"+title+",value:"+value;}
}

class SSlider{

    private int id;
    public void setId(int id) {this.id = id;}
    public int getId() {return id;}

    private String title;
    public void setTitle(String title) {this.title = title;}
    public String getTitle() {return title;}

    private int min;
    public void setMin(int min){this.min = min;}
    public int getMin(){return min;}

    private int max;
    public void setMax(int max){this.max = max;}
    public int getMax(){return max;}

    private int step;
    public void setStep(int step){this.step = step;}
    public int getStep(){return step;}

    private int value;
    public void setValue(int value){this.value = value;}
    public int getValue(){return value;}

    private int conversionFactor;
    public void setConversionFactor(int conversionFactor) {this.conversionFactor = conversionFactor;}
    public int getConversionFactor() {return conversionFactor;}

    public String toString() {return "slider="+"id:"+id+",title:"+title+",min:"+min+",max:"+max+",step:"+step+",conversionFactor:"+conversionFactor+",value:"+value;}
}

class SDropdown {

    int id;
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    String title;
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    int value;
    public void setValue(int value){this.value = value;}
    public int getValue(){return value;}

    ArrayList<SDropdownOption> options = new ArrayList<SDropdownOption>();

}

class SDropdownOption {

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

class SSensor {

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
