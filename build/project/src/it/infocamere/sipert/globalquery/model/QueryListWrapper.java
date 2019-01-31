package it.infocamere.sipert.globalquery.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "queries")
public class QueryListWrapper {

    private List<QueryModel> queries;

    @XmlElement(name = "query")
    public List<QueryModel> getQueries() {
        return queries;
    }

    public void setQueries(List<QueryModel> queries) {
        this.queries = queries;
    }
}
