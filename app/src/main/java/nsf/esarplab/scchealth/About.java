package nsf.esarplab.scchealth;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView project=(TextView) findViewById(R.id.about);
        //show actionbar
        ActionBar myActionBar = getSupportActionBar();
        myActionBar.show();

        String projectdescription="";
        projectdescription+="Project Title: EAGER: Events-of-interest Capture Using Novel Body-worn " +
                "Fully-passive Wireless sensors for S&CC\n"+"\n";;
        projectdescription+="Funding Source: NSF CISE CNS\n  " +"\n";
        projectdescription+="Project Duration: 2016 - 2018\n " +"\n";
        projectdescription+="Project Synopsis:" +
                "\n" +
                "Patients with chronic illness require frequent and avoidable hospital visits. " +
                "This project aims to develop a new class of battery-less, low-cost, disposable, " +
                "wireless electronic patch sensors to monitor a variety of physiological signals and " +
                "a custom smartphone app to monitor their health status and to elect to share their" +
                " anonymized events-of-interest with their community towards a smart and connected " +
                "community (S&CC). This will empower users, permit the community stakeholders to assess" +
                " population health status, reduce the need for frequent hospital visits, and help " +
                "identify potential individual and community actions to achieve improvement in health" +
                " status. The project also involves the training of undergraduate and graduate students" +
                " in interdisciplinary research activities on emerging technologies, and is expected to" +
                " impact public and private sector efforts to improve healthcare.\n" +
                "\n" +
                "PI: Dr. Bashir Morshed, Associate Professor, Department of Electrical and Computer " +
                "Engineering, The University of Memphis\n" +
                "\n" +
                "Co-PI: Dr. Brook Harmon, Assistant Professor, School of Public Health, The University of Memphis"+"\n\n"
                +"Consultant: Dr. M. Rahman,  Baptist Minor Medical Center, Memphis, TN"+"\n\n"
                +"Collaborator: Memphis District of The United Methodist Church (UMC)";
        project.setText(projectdescription);


    }
}