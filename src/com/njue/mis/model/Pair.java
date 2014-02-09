package com.njue.mis.model;

public class Pair<LType extends Object, RType extends Object> {
	private LType left;
	private RType right;
	
	public Pair(){
		
	}
	
	public Pair(LType left, RType right){
		this.left = left;
		this.right = right;
	}
	
	@Override
	public int hashCode(){
		return (left.toString()+right.toString()).hashCode();
	}
	
	@Override
	public boolean equals(Object object){
		if(!(object instanceof Pair)){
			return false;
		}
		@SuppressWarnings("unchecked")
		Pair<LType, RType> otherPair = (Pair<LType, RType>) object;
		return left.equals(otherPair.getLeft())&&right.equals(otherPair.getRight());
	}
	
	public LType getLeft() {
		return left;
	}

	public void setLeft(LType left) {
		this.left = left;
	}

	public RType getRight() {
		return right;
	}

	public void setRight(RType right) {
		this.right = right;
	}
	
	
}
