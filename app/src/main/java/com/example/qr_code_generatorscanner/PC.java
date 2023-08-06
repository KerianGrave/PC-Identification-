package com.example.qr_code_generatorscanner;

public class PC {
    String pcid, pcno, model, processor, ram, ssd;

    public PC() {
    }

    public PC( String pcid, String pcno, String model, String processor, String ram, String ssd) {
        this.pcid = pcid;
        this.pcno = pcno;
        this.model = model;
        this.processor = processor;
        this.ram = ram;
        this.ssd = ssd;
    }

    public String getPcid() {
        return pcid;
    }

    public void setPcid(String pcid) {
        this.pcid = pcid;
    }

    public void setPcno(String pcno) {
        this.pcno = pcno;
    }

    public String getPcno() {
        return pcno;
    }

    public void setPcno(){
        this.pcno = pcno;
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
}
