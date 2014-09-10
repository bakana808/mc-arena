package com.octopod.arenacore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Octopod - octopodsquad@gmail.com
 */
public class Vector implements Cloneable
{
	public double X, Y, Z;

	public Vector(double x, double y, double z)
	{
		this.X = x;
		this.Y = y;
		this.Z = z;
	}

	public String toString() {return "[" + X + ", " + Y + ", " + Z + "]";}

	@Override
	public Vector clone() throws CloneNotSupportedException
	{
		Vector vec;
		try{
			vec = (Vector)super.clone();
		} catch (CloneNotSupportedException e) {
			vec = new Vector(X, Y, Z);
		}
		return vec;
	}

	public double magnitude() {return Math.sqrt(Math.pow(X, 2) + Math.pow(Y, 2) + Math.pow(Z, 2));}

	//Converts into an ArrayList
	public List<Double> asList()
	{
		List<Double> vec = new ArrayList<Double>();
		vec.add(X); vec.add(Y); vec.add(Z);
		return vec;
	}

	//Converts current vector to have a magnitude of 1 (the direction stays the same)
	public void normalize()
	{
		double m = magnitude();
		if(m == 0) return;
		X /= m; Y /= m; Z /= m;
	}

	//Calculates distance from another vector
	public double distance(Vector vec)
	{
		double x = Math.pow(X - vec.X, 2);
		double y = Math.pow(Y - vec.Y, 2);
		double z = Math.pow(Z - vec.Z, 2);
		return Math.sqrt(x + y + z);
	}

	//Multiplies all indexes by 'X'
	public Vector mult(double x) {
		X *= x; Y *= x; Z *= x;
		return this;
	}

	//Adds current vector to another vector
	public Vector add(Vector vec) {
		X += vec.X; Y += vec.Y; Z += vec.Z;
		return this;
	}

	public Vector add(double X, double Y, double Z) {return add(new Vector(X, Y, Z));}

	public void floor()
	{
		X = Math.floor(X);
		Y = Math.floor(Y);
		Z = Math.floor(Z);
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof Vector)) return false;
		Vector vec = (Vector)obj;
		return vec.X == X && vec.Y == Y && vec.Z == Z;
	}

}
