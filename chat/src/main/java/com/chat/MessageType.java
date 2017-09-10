package com.chat;

public enum MessageType {
	GeneratedId (1),
	Message (2),
	Photo(3),
	Video(4),
	Sync(5);
	
	private final int valor;
	MessageType(int tipo){
		valor = tipo;
	}
	public int getValor(){
		return valor;
	}
}
