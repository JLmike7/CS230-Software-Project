package edu.ycp.casino.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.LayoutPanel;

import edu.ycp.casino.shared.Observable;
import edu.ycp.casino.shared.Observer;
import edu.ycp.casino.shared.Player;
import edu.ycp.casino.shared.cardgame.Card;
import edu.ycp.casino.shared.cardgame.poker.Table;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.NumberLabel;

public class PokerViewGWT extends Composite implements Observer{

	private boolean allIn;
	private boolean gameOverShown;
	private boolean showNewGameAlert;
	private Table table;
	private Label lblMessage;
	private com.google.gwt.user.client.ui.Image image;
	private com.google.gwt.user.client.ui.Image image_1;
	private com.google.gwt.user.client.ui.Image image_2;
	private com.google.gwt.user.client.ui.Image image_3;
	private com.google.gwt.user.client.ui.Image image_6;
	private com.google.gwt.user.client.ui.Image image_4;
	private com.google.gwt.user.client.ui.Image image_5;
	private NumberLabel<Integer> lblMin;
	private NumberLabel<Integer> lblMax;
	private IntegerBox betBox;
	
	// TODO: add fields to store state
	
	// constructor
	public PokerViewGWT() {
		LayoutPanel layout=new LayoutPanel();
		initWidget(layout);
		layout.setSize("489px", "461px");
		
		ClickHandler betHandler=new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//is bet button
				int bet=betBox.getValue();
				table.getCurrentPlayer().setHoldingBet(bet,lblMin.getValue());
				if(table.getCurrentPlayer().takeHoldingBet()){
					if(allIn)
						table.getPot().setMaxBet(bet);
					if (table.getPot().add(bet)){
						lblMessage.setText("Player "+table.getCurrentPlayerNum()+" added $"+bet+" to the pot");
						table.iterateCurrentPlayer();
					}
					else
						
						lblMessage.setText("Invalid bet.");
					allIn=false;
				}
				else
					lblMessage.setText("Not enough funds.");
				clearBet();
			}
		};
		ClickHandler checkHandler=new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//is check button
				if (table.getPot().getMinBet()==0){
					table.getPot().add(0);
					table.getCurrentPlayer().setHoldingBet(0,0);
					lblMessage.setText("Player "+table.getCurrentPlayerNum()+" checks");
					table.iterateCurrentPlayer();
					clearBet();
				}
				else
					lblMessage.setText("Invalid bet.");
			}
		};
		ClickHandler foldHandler=new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//is fold button
				table.getCurrentPlayer().fold();
				lblMessage.setText("Player "+table.getCurrentPlayerNum()+" folds.");
				if(table.nonFoldedPlayers()>0)
					table.iterateCurrentPlayer();
				else
					newGame();
			}
		};
		
		Button btnBet = new Button("Bet");
		btnBet.addClickHandler(betHandler);
		layout.add(btnBet);
		layout.setWidgetLeftWidth(btnBet, 144.0, Unit.PX, 81.0, Unit.PX);
		layout.setWidgetTopHeight(btnBet, 226.0, Unit.PX, 30.0, Unit.PX);
		
		Button btnCheck = new Button("Check");
		btnCheck.addClickHandler(checkHandler);
		layout.add(btnCheck);
		layout.setWidgetLeftWidth(btnCheck, 144.0, Unit.PX, 81.0, Unit.PX);
		layout.setWidgetTopHeight(btnCheck, 190.0, Unit.PX, 30.0, Unit.PX);
		
		Button btnFold = new Button("Fold");
		btnFold.addClickHandler(foldHandler);
		layout.add(btnFold);
		layout.setWidgetLeftWidth(btnFold, 57.0, Unit.PX, 81.0, Unit.PX);
		layout.setWidgetTopHeight(btnFold, 226.0, Unit.PX, 30.0, Unit.PX);
		
		image = new com.google.gwt.user.client.ui.Image("cards/back.png");
		layout.add(image);
		layout.setWidgetLeftWidth(image, 0.0, Unit.PX, 126.0, Unit.PX);
		layout.setWidgetTopHeight(image, 58.0, Unit.PX, 156.0, Unit.PX);
		
		Label lblBetAmount = new Label("Bet Amount");
		lblBetAmount.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		layout.add(lblBetAmount);
		layout.setWidgetLeftWidth(lblBetAmount, 0.0, Unit.PX, 138.0, Unit.PX);
		layout.setWidgetTopHeight(lblBetAmount, 170.0, Unit.PX, 18.0, Unit.PX);
		
		image_1 = new com.google.gwt.user.client.ui.Image("cards/back.png");
		layout.add(image_1);
		layout.setWidgetLeftWidth(image_1, 77.0, Unit.PX, 71.0, Unit.PX);
		layout.setWidgetTopHeight(image_1, 58.0, Unit.PX, 96.0, Unit.PX);
		
		image_2 = new com.google.gwt.user.client.ui.Image("cards/back.png");
		layout.add(image_2);
		layout.setWidgetLeftWidth(image_2, 154.0, Unit.PX, 71.0, Unit.PX);
		layout.setWidgetTopHeight(image_2, 58.0, Unit.PX, 96.0, Unit.PX);
		
		image_3 = new com.google.gwt.user.client.ui.Image("cards/back.png");
		layout.add(image_3);
		layout.setWidgetLeftWidth(image_3, 231.0, Unit.PX, 71.0, Unit.PX);
		layout.setWidgetTopHeight(image_3, 58.0, Unit.PX, 96.0, Unit.PX);
		
		image_4 = new com.google.gwt.user.client.ui.Image("cards/back.png");
		layout.add(image_4);
		layout.setWidgetLeftWidth(image_4, 231.0, Unit.PX, 71.0, Unit.PX);
		layout.setWidgetTopHeight(image_4, 160.0, Unit.PX, 96.0, Unit.PX);
		
		image_5 = new com.google.gwt.user.client.ui.Image("cards/back.png");
		layout.add(image_5);
		layout.setWidgetLeftWidth(image_5, 308.0, Unit.PX, 71.0, Unit.PX);
		layout.setWidgetTopHeight(image_5, 160.0, Unit.PX, 96.0, Unit.PX);
		
		image_6 = new com.google.gwt.user.client.ui.Image("cards/back.png");
		layout.add(image_6);
		layout.setWidgetLeftWidth(image_6, 308.0, Unit.PX, 71.0, Unit.PX);
		layout.setWidgetTopHeight(image_6, 58.0, Unit.PX, 96.0, Unit.PX);
		
		Label label = new Label("$");
		label.setWordWrap(false);
		layout.add(label);
		layout.setWidgetLeftWidth(label, 0.0, Unit.PX, 10.0, Unit.PX);
		layout.setWidgetTopHeight(label, 194.0, Unit.PX, 26.0, Unit.PX);
		
		lblMessage = new Label("Welcome to Poker!");
		lblMessage.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		layout.add(lblMessage);
		layout.setWidgetLeftWidth(lblMessage, 0.0, Unit.PX, 379.0, Unit.PX);
		layout.setWidgetTopHeight(lblMessage, 0.0, Unit.PX, 18.0, Unit.PX);
		
		Label lblMinimumBet = new Label("Minimum Bet:");
		layout.add(lblMinimumBet);
		layout.setWidgetLeftWidth(lblMinimumBet, 231.0, Unit.PX, 88.0, Unit.PX);
		layout.setWidgetTopHeight(lblMinimumBet, 18.0, Unit.PX, 18.0, Unit.PX);
		
		Label lblMaximumBet = new Label("Maximum Bet:");
		layout.add(lblMaximumBet);
		layout.setWidgetLeftWidth(lblMaximumBet, 231.0, Unit.PX, 88.0, Unit.PX);
		layout.setWidgetTopHeight(lblMaximumBet, 34.0, Unit.PX, 18.0, Unit.PX);
		
		lblMin = new NumberLabel<Integer>();
		layout.add(lblMin);
		layout.setWidgetLeftWidth(lblMin, 325.0, Unit.PX, 62.0, Unit.PX);
		layout.setWidgetTopHeight(lblMin, 18.0, Unit.PX, 18.0, Unit.PX);
		
		lblMax = new NumberLabel<Integer>();
		layout.add(lblMax);
		layout.setWidgetLeftWidth(lblMax, 325.0, Unit.PX, 62.0, Unit.PX);
		layout.setWidgetTopHeight(lblMax, 34.0, Unit.PX, 18.0, Unit.PX);
		
		betBox = new IntegerBox();
		layout.add(betBox);
		layout.setWidgetLeftWidth(betBox, 10.0, Unit.PX, 128.0, Unit.PX);
		layout.setWidgetTopHeight(betBox, 194.0, Unit.PX, 26.0, Unit.PX);
		
		com.google.gwt.user.client.ui.Image image_7 = new com.google.gwt.user.client.ui.Image((String) null);
		layout.add(image_7);
		layout.setWidgetLeftWidth(image_7, 122.0, Unit.PX, 100.0, Unit.PX);
		layout.setWidgetTopHeight(image_7, 318.0, Unit.PX, 100.0, Unit.PX);
	}
	public void clearBet(){
		
	}
	public void newGame(){
		
	}
	public void update(Observable g, Object hint) {

	}
}