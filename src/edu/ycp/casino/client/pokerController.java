package edu.ycp.casino.client;

import java.awt.event.MouseEvent;

import edu.ycp.casino.shared.cardgame.poker.Table;

public class pokerController {
	PokerViewGWT pokerView;
	Table table;
	public pokerController(PokerViewGWT _pokerView,Table _table){
		this.pokerView=_pokerView;
		this.table=_table;
	}
	public PokerViewGWT getPokerView() {
		return pokerView;
	}
	public void setPokerView(PokerViewGWT pokerView) {
		this.pokerView = pokerView;
	}
	public Table getTable() {
		return table;
	}
	public void setTable(Table table) {
		this.table = table;
	}
}
