/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package no.nb.microservice.dewey.rest.model;

import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author jimn
 */
public class Record {
    private String level;
    private String deweyClass;
    private String heading;

    @XmlElement(name = "level")
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @XmlElement(name = "class")
    public String getDeweyClass() {
        return deweyClass;
    }

    public void setDeweyClass(String deweyClass) {
        this.deweyClass = deweyClass;
    }

    @XmlElement(name = "heading")
    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }
        
}
