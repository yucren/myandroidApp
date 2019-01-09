/**
  * Copyright 2019 bejson.com 
  */
package com.example.yucren.myapplication.tools.version;
import java.util.List;

/**
 * Auto-generated: 2019-01-09 13:37:3
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class ApkInfo {

    private String type;
    private List<String> splits;
    private int versionCode;
    private String versionName;
    private boolean enabled;
    private String outputFile;
    private String fullName;
    private String baseName;
    public void setType(String type) {
         this.type = type;
     }
     public String getType() {
         return type;
     }

    public void setSplits(List<String> splits) {
         this.splits = splits;
     }
     public List<String> getSplits() {
         return splits;
     }

    public void setVersionCode(int versionCode) {
         this.versionCode = versionCode;
     }
     public int getVersionCode() {
         return versionCode;
     }

    public void setVersionName(String versionName) {
         this.versionName = versionName;
     }
     public String getVersionName() {
         return versionName;
     }

    public void setEnabled(boolean enabled) {
         this.enabled = enabled;
     }
     public boolean getEnabled() {
         return enabled;
     }

    public void setOutputFile(String outputFile) {
         this.outputFile = outputFile;
     }
     public String getOutputFile() {
         return outputFile;
     }

    public void setFullName(String fullName) {
         this.fullName = fullName;
     }
     public String getFullName() {
         return fullName;
     }

    public void setBaseName(String baseName) {
         this.baseName = baseName;
     }
     public String getBaseName() {
         return baseName;
     }

}