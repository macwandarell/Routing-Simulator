package modules.network.Link;

import modules.models.Model;

public class Link {
    private Model first;
    private Model second;
    private int bandwidth;

    public Link(Model first,Model second, int bandwidth){
        this.first = first;
        this.second = second;
        this.bandwidth = bandwidth;
    }

    public Model getFirst() {
        return first;
    }

    public void setFirst(Model first) {
        this.first = first;
    }

    public Model getSecond() {
        return second;
    }

    public void setSecond(Model second) {
        this.second = second;
    }

    public int getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(int bandwidth) {
        this.bandwidth = bandwidth;
    }
}
