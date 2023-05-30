package com.tut.idc.library.model.report;

import com.tut.idc.library.util.annotation.ColumnName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackReport {
    @ColumnName(name = "ID Number")
    private String IDNumber;
    @ColumnName(name = "Stars")
    private int stars;
    @ColumnName(name = "Comment")
    private String comment;
    @ColumnName(name = "Date Posted")
    private String date;

}
