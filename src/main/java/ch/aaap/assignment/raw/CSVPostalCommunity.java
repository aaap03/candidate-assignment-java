package ch.aaap.assignment.raw;

public record CSVPostalCommunity(String zipCode,                        //PLZ4
                                 String zipCodeAddition,                //PLZZ
                                 String name,                           //PLZNAMK
                                 String cantonCode,                     //KTKZ
                                 String politicalCommunityNumber,       //GDENR
                                 String politicalCommunityShortName) {  //GDENAMK
}
