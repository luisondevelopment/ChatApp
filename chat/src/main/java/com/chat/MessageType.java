package com.chat;

public enum MessageType {
	GeneratedId (1),
	Message (2);
	
	private final int valor;
	MessageType(int tipo){
		valor = tipo;
	}
	public int getValor(){
		return valor;
	}
}
