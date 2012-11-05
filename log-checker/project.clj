(defproject com.redhat.qe/log-checker "1.0.0-SNAPSHOT"
  :description "TestNG Listener with annotations for checking local and remote log files"
  :java-source-paths ["src/main/java"]
  :javac-options {:debug "on"}
  :dependencies [[org.testng/testng "6.5.1"] 
                 [com.redhat.qe/ssh-tools "1.0.0"]])
