package ukma.project.fifam.dtos.fillers;

import ukma.project.fifam.Frequency;
import ukma.project.fifam.models.Category;

import java.util.Comparator;

public class PeriodicBalanceFiller {
    public PeriodicFillerType type;
    public Double sum;
    public Double lastBalance;
    public Frequency frequency;
    public long lastPayDate;
    public String name;
    public Category category;
    public long id;

    public PeriodicBalanceFiller(PeriodicFillerType type, String sum, Frequency freq, long lastPayDate, String name, Category category, long id){
        this.type = type;
        this.sum = Double.parseDouble(sum);
        this.frequency = freq;
        this.lastPayDate = lastPayDate;
        this.name = name;
        this.lastBalance = 0.0;
        this.category = category;
        this.id = id;
    }

    public static Comparator<PeriodicBalanceFiller> GetComparator(){
        return new Comparator<PeriodicBalanceFiller>()
        {

            public int compare(PeriodicBalanceFiller node1, PeriodicBalanceFiller node2)
            {
                if (node1.lastPayDate< node2.lastPayDate)
                {
                    return -1;
                }
                else if (node1.lastPayDate > node2.lastPayDate)
                {
                    return 1;
                }

                return 0;
            }
        };
    }
}
