package com.chat;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
	private static final long serialVersionUID = 2L;

	private int Id;
	private String Message;
    private String Nome;
    private Date Data;
	private MessageType type;
	private boolean IsSync;
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public MessageType getType() {
		return type;
	}
	public void setType(MessageType type) {
		this.type = type;
	}

	public String getNome() {
		return Nome;
	}

	public void setNome(String nome) {
		Nome = nome;
	}

	public Date getData() {
		return Data;
	}

	public void setData(Date data) {
		Data = data;
	}

	public boolean isSync() {
		return IsSync;
	}

	public void setSync(boolean sync) {
		IsSync = sync;
	}

	public static enum MessageType {
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
}
