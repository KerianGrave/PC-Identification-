package com.example.qr_code_generatorscanner;

public class Users {
    String pcno,model, processor, ram, ssd;

    public Users() {
    }

    public Users(String pcno,String model, String processor, String ram, String ssd) {
        this.pcno = pcno;
        this.model = model;
        this.processor = processor;
        this.ram = ram;
        this.ssd = ssd;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getSsd() {
        return ssd;
    }

    public void setSsd(String ssd) {
        this.ssd = ssd;
    }

    public void setPcno(String pcno){this.pcno = pcno;}

    public String getPcno(){return pcno;}
}
