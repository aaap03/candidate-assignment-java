package ch.aaap.assignment.raw;

import java.time.LocalDate;

public record CSVPoliticalCommunity(String number,                                //GDENR
                                    String name,                                  //GDENAME
                                    String shortName,                             //GDENAMK
                                    String cantonCode,                            //GDEKT
                                    String cantonName,                            //GDEKTNA
                                    String districtNumber,                        //GDEBZNR
                                    String districtName,                          //GDEBZNA
                                    LocalDate lastUpdate) {                       //GDEMUTDA
}
