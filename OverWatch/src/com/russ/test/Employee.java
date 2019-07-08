package com.russ.test;

/**
*
* @author www.codejava.net
*
*/
public class Employee {
   private int index;
   private String name;
   private String job;
   private int age;

   public Employee(String name, String job, int age) {
       this.name = name;
       this.job = job;
       this.age = age;
   }

/**
 * @return the index
 */
public int getIndex() {
	return index;
}

/**
 * @param index the index to set
 */
public void setIndex(int index) {
	this.index = index;
}

/**
 * @return the name
 */
public String getName() {
	return name;
}

/**
 * @param name the name to set
 */
public void setName(String name) {
	this.name = name;
}

/**
 * @return the job
 */
public String getJob() {
	return job;
}

/**
 * @param job the job to set
 */
public void setJob(String job) {
	this.job = job;
}

/**
 * @return the age
 */
public int getAge() {
	return age;
}

/**
 * @param age the age to set
 */
public void setAge(int age) {
	this.age = age;
}
    


}