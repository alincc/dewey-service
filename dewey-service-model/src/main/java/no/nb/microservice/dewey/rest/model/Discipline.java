/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package no.nb.microservice.dewey.rest.model;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 *
 * @author jimn
 */
public class Discipline {
    private List<Record> record;

    @XmlElement(name = "record")
    public List<Record> getRecord() {
        return record;
    }

    public void setRecord(List<Record> record) {
        this.record = record;
    }
}
