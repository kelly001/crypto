package database;

/**
 * Created by Julia on 14.11.2014.
 */
public class Employer extends User {
    protected Company company;

    public Employer() {
        super();
        this.company = new Company();
    }
    public void setCompany(Company company) {this.company=company;}
    public Company getCompany() {return company;}
}
