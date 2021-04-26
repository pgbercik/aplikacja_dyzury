package com.example.aplikacja_dyzury.user.userCalendar.csv;

import com.example.aplikacja_dyzury.common.user_registration_ui.GoogleCalendarPoJo;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvUtils {
    public static void writeDataLineByLine(String filePath, List<GoogleCalendarPoJo> googleCalendarPoJoList)
    {

        File file = new File(filePath);
        try {

            FileWriter outputfile = new FileWriter(file);


            CSVWriter writer = new CSVWriter(outputfile);

            // nagłówek
            String[] header = { "Subject", "Start Date", "Start Time","End Date","End Time","Description","Location" };
            writer.writeNext(header);

            // dodajemy dane do CSV
            for (GoogleCalendarPoJo googleCalendarPoJo1 :googleCalendarPoJoList){
                String[] data = {googleCalendarPoJo1.getSubjectTitle(),googleCalendarPoJo1.getStartDate(),googleCalendarPoJo1.getStartTime(),
                googleCalendarPoJo1.getEndDate(),googleCalendarPoJo1.getEndTime(),googleCalendarPoJo1.getDescription(),googleCalendarPoJo1.getLocation()};
                writer.writeNext(data);
            }



            // zamykamy writera
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}