/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uk.ac.westminster.w2115918.smartcampuscw;

public class SensorReading {

    private String id;
    private String sensorId;
    private Double value;
    private String timestamp;

    public SensorReading() {}

    public SensorReading(String id, String sensorId, Double value, String timestamp) {
        this.id = id;
        this.sensorId = sensorId;
        this.value = value;
        this.timestamp = timestamp;
    }

    public String getId()                      { return id; }
    public void setId(String id)               { this.id = id; }

    public String getSensorId()                { return sensorId; }
    public void setSensorId(String sensorId)   { this.sensorId = sensorId; }

    public Double getValue()                   { return value; }
    public void setValue(Double value)         { this.value = value; }

    public String getTimestamp()               { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
