/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package no.nb.microservice.dewey.rest.model;

import org.springframework.hateoas.ResourceSupport;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jimn
 */
public class DeweyWrapper extends ResourceSupport {
    private List<Dewey> deweyList;
    private List<Dewey> deweyPathList;

    public DeweyWrapper() {
        this.deweyPathList = new ArrayList<>();
    }

    public List<Dewey> getDeweyList() {
        return deweyList;
    }

    public void setDeweyList(List<Dewey> deweyList) {
        this.deweyList = deweyList;
    }

    public List<Dewey> getDeweyPathList() {
        return deweyPathList;
    }

    public void setDeweyPathList(List<Dewey> deweyPathList) {
        this.deweyPathList = deweyPathList;
    }
    
    
}
