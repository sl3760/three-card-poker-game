package com.poker.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.poker.shared.Card;
import com.poker.shared.PlayerMove.Choose;
import com.poker.shared.PlayerMove.Decision;
import com.poker.client.GameSounds;
import com.google.gwt.media.client.Audio;
import com.google.gwt.dom.client.AudioElement;
import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.event.dom.client.ChangeEvent;
public class Graphics extends Composite implements Presenter.View {

	private static GraphicsUiBinder uiBinder = GWT
			.create(GraphicsUiBinder.class);
	static GameMessages messages = (GameMessages) GWT.create(GameMessages.class);
	interface GraphicsUiBinder extends UiBinder<Widget, Graphics> {
	}

	public Graphics(Presenter presenter) {
		initWidget(uiBinder.createAndBindUi(this));
		this.presenter = presenter;
		start.setText(messages.setAutoStart());
		restart.setText(messages.setRestartGame());
		save.setText(messages.setSave());
		load.setText(messages.setLoad());
		PairsPlusBtn.setText(messages.setPairsPlus());
		AnteBtn.setText(messages.setAnte());
		PairsPlusAnteBtn.setText(messages.setPairsPlusAnte());
		dealBtn.setText(messages.setDeal());
		playBtn.setText(messages.setPlay());
		foldBtn.setText(messages.setFold());
		cancel.setText(messages.setCancel());
		newMatch.setText(messages.setStartMatch());
		deleteMatch.setText(messages.setDeleteMatch());    
        matchList.addItem(messages.setSelectMatch());
        matchList.setVisibleItemCount(1);
        emailInput.setWidth("150px");  
        signOutLink.setText(messages.setSignout());
        AIPlay.setText(messages.setAIPlay());
        
	}

	private Presenter presenter;
	private CardMovingAnimation animation;
	private GameImageSupplier gameimages = new GameImageSupplier();
	CardImages cardImages = GWT.create(CardImages.class);
	private CardImageSupplier cardimages = new CardImageSupplier(cardImages);
	private static GameSounds gameSounds = GWT.create(GameSounds.class);
	private static ChipImages chipImages = GWT.create(ChipImages.class);

	private static final int NAME_HEIGHT = 20;
	private static final int CARD_HEIGHT = 100;
	private static final int CARD_WIDTH = 80;
	ArrayList<Label> namePlaces = new ArrayList<Label>();
	ArrayList<FlowPanel> cardPlaces = new ArrayList<FlowPanel>();

	private Audio startSound;
	private Audio restartSound;
	private Audio chooseSound;
	private Audio decisionSound;
	private Audio warningSound;
	private Image chip10 = new Image(chipImages.chip10());
	private Image chip20 = new Image(chipImages.chip20());
	private Image chip50 = new Image(chipImages.chip50());
	private Image chip100 = new Image(chipImages.chip100());
	private Image PairsPlus;
	private Image Ante;
	private PickupDragController dragController;
	private SimpleDropController dropController;
	private int chipPairsPlus=0;
	private int chipAnte=0;
	
	@UiField
	AbsolutePanel backgroundArea;
	@UiField
	HorizontalPanel buttonPanel;
	@UiField
	HorizontalPanel buttonChoosePanel;
	@UiField
	HorizontalPanel buttonDecisionPanel;
	@UiField
	Button PairsPlusBtn;
	@UiField
	Button AnteBtn;
	@UiField
	Button PairsPlusAnteBtn;
	@UiField
	Button dealBtn;
	@UiField
	Button playBtn;
	@UiField
	Button foldBtn;
	@UiField
	Label gameInfo;
	@UiField
	Label gameTurn;
	@UiField
	Label gameWarning;
	@UiField
	Label balance1;
	@UiField
	Label balance2;
	@UiField
	Button start;
	@UiField
	Button restart;
	@UiField
	Button save;
	@UiField
	Button load;	
	@UiField
	Button cancel;
	
	@UiField
	Label Connect;
	@UiField 
	Label NickName;
	@UiField
	Label Email;
	@UiField
	Label rank;
    @UiField
    Label OtherEmail;
    @UiField
    Label OtherNickName;
    @UiField
    Label Turn;
    
    @UiField
    ListBox matchList;
    @UiField
    TextBox emailInput;
    @UiField
    Button newMatch;
    @UiField
    Button deleteMatch;
    @UiField
    Anchor signOutLink;
    @UiField
    Button AIPlay;
    @UiField
    Label computerChoose;
    @UiField
    Label computerChip;
    @UiField
    Label computerDecision;
    
    /**
     * below are UiHandlers
     * @param e
     */
    @UiHandler("AIPlay")
    void onAIPlay(ClickEvent e) {
    	presenter.AIStartGame();
    }
    
    @UiHandler("start")
	void onStartBtn(ClickEvent e) {
		if (Audio.isSupported()) {
			
			startSound = Audio.createIfSupported();
			
			startSound.addSource(gameSounds.StartMp3().getSafeUri().asString(),
					AudioElement.TYPE_MP3);
			startSound.addSource(gameSounds.StartWav().getSafeUri().asString(),
					AudioElement.TYPE_WAV);
			startSound.addSource(gameSounds.StartOgg().getSafeUri().asString(),
					AudioElement.TYPE_OGG);
			
			startSound.play();

		}
		presenter.startGame();
		start.setEnabled(false);
	}

	@UiHandler("restart")
	void onReStartBtn(ClickEvent e) {
		if (Audio.isSupported()) {
			restartSound = Audio.createIfSupported();
			restartSound.addSource(gameSounds.RestartMp3().getSafeUri()
					.asString(), AudioElement.TYPE_MP3);
			restartSound.addSource(gameSounds.RestartOgg().getSafeUri()
					.asString(), AudioElement.TYPE_OGG);		
			restartSound.addSource(gameSounds.RestartWav().getSafeUri()
					.asString(), AudioElement.TYPE_WAV);
			restartSound.play();

		}

		backgroundArea.clear();
		cardPlaces.clear();
		presenter.reStartGame();
	}
	
    @UiHandler("PairsPlusBtn")
	void onClickPairsPlusBtn(ClickEvent e) {
	   if(presenter.getState().getPlayerTurn()==1){
			dragController.makeNotDraggable(chip10);
			dragController.makeNotDraggable(chip20);
			dragController.makeNotDraggable(chip50);
			dragController.makeNotDraggable(chip100);
			dragController.unregisterDropController(dropController);
			cancel.setEnabled(false);
			presenter.setDrag(false);
		}
		presenter.doChoose(Choose.PairsPlus,chipPairsPlus, chipAnte);
		chipPairsPlus=0;
		chipAnte=0;
		save.setEnabled(true);
		load.setEnabled(true);
	}

	@UiHandler("AnteBtn")
	void onClickAnteBtn(ClickEvent e) {
		if(presenter.getState().getPlayerTurn()==1){
			dragController.makeNotDraggable(chip10);
			dragController.makeNotDraggable(chip20);
			dragController.makeNotDraggable(chip50);
			dragController.makeNotDraggable(chip100);
			dragController.unregisterDropController(dropController);
			cancel.setEnabled(false);
			presenter.setDrag(false);
		}
		presenter.doChoose(Choose.Ante, chipPairsPlus, chipAnte);
		chipPairsPlus=0;
		chipAnte=0;
		save.setEnabled(true);
		load.setEnabled(true);
	}

	@UiHandler("PairsPlusAnteBtn")
	void onClickPairsPlusAnteBtn(ClickEvent e) {
		if(presenter.getState().getPlayerTurn()==1){
			dragController.makeNotDraggable(chip10);
			dragController.makeNotDraggable(chip20);
			dragController.makeNotDraggable(chip50);
			dragController.makeNotDraggable(chip100);
			dragController.unregisterDropController(dropController);
			cancel.setEnabled(false);
			presenter.setDrag(false);
		}
		presenter.doChoose(Choose.PairsPlusAnte, chipPairsPlus, chipAnte);
		chipPairsPlus=0;
		chipAnte=0;
		save.setEnabled(true);
		load.setEnabled(true);
	}

	@UiHandler("dealBtn")
	void onClickDealBtn(ClickEvent e) {
		presenter.doMove(Decision.Deal);
	}

	@UiHandler("playBtn")
	void onClickPlayBtn(ClickEvent e) {
		presenter.doMove(Decision.Play);
	}

	@UiHandler("foldBtn")
	void onClickFoldBtn(ClickEvent e) {
		presenter.doMove(Decision.Fold);
	}

	@UiHandler("save")
	void onClickSave(ClickEvent e) {			
		presenter.saveGame("local");
	}
	
	@UiHandler("load")
	void onClickLoad(ClickEvent e) {			
		presenter.loadGame("local");
	}
	
	@UiHandler("cancel")
	void onClickCancel(ClickEvent e) {
         presenter.cancel();
	}
	
	@UiHandler("newMatch")
	void onClickNewMatch (ClickEvent e){
		presenter.newMatch();
	}
	
	@UiHandler("deleteMatch")
   	void onClickDelete(ClickEvent e){
	    presenter.deleteMatch();	
	}
	
	@UiHandler("matchList")
	void onChangeMatchList(ChangeEvent e){
		presenter.getMatchList();
	}
	
	@Override
	public void setPresenter(Presenter p) {
		this.presenter = p;
	}

	public Presenter getPresenter() {
		return this.presenter;
	}

	@Override
	public Widget getWidget() {
		return this;
	}
	/**
	 * set background 
	 */
	@Override
	public void setBackground(ArrayList<String> playerIds) {
		// set the background image
		Image pokertable = gameimages.getPokerTable();
		int picWidth = pokertable.getWidth();
		int picHeight = pokertable.getHeight();
		int numPlayer = playerIds.size();
		backgroundArea.setPixelSize(picWidth, picHeight);
		backgroundArea.add(pokertable);
		// set chip images
		int chipWidth = 60;
		int chipHeight = 60;

		chip10.setPixelSize(chipWidth, chipHeight);
		backgroundArea.add(chip10);
		backgroundArea.setWidgetPosition(chip10, 300, 450);

		chip20.setPixelSize(chipWidth, chipHeight);
		backgroundArea.add(chip20);
		backgroundArea.setWidgetPosition(chip20, 400, 450);

		chip50.setPixelSize(chipWidth, chipHeight);
		backgroundArea.add(chip50);
		backgroundArea.setWidgetPosition(chip50, 500, 450);

		chip100.setPixelSize(chipWidth, chipHeight);
		backgroundArea.add(chip100);
		backgroundArea.setWidgetPosition(chip100, 600, 450);

		PairsPlus = new Image(chipImages.PairsPlus());
		PairsPlus.setPixelSize(chipWidth + 40, chipHeight + 40);
		backgroundArea.add(PairsPlus);
		backgroundArea.setWidgetPosition(PairsPlus, 380, 300);

		Ante = new Image(chipImages.Ante());
		Ante.setPixelSize(chipWidth + 40, chipHeight + 40);
		backgroundArea.add(Ante);
		backgroundArea.setWidgetPosition(Ante, 500, 300);

		// set dealer's place
		int dealerNameTop = (int) (pokertable.getHeight() * 0.22);
		int dealerCardTop = dealerNameTop + NAME_HEIGHT;

		namePlaces.add(new Label(messages.setDealer()));
		cardPlaces.add(new FlowPanel());
		int left = (int) (0.5 * picWidth - 0.5 * CARD_WIDTH);
		backgroundArea.add(namePlaces.get(0));
		backgroundArea
				.setWidgetPosition(namePlaces.get(0), left, dealerNameTop);

		cardPlaces.get(0).setPixelSize(CARD_WIDTH * 3, CARD_HEIGHT);
		backgroundArea.add(cardPlaces.get(0));
		backgroundArea
				.setWidgetPosition(cardPlaces.get(0), left, dealerCardTop);

		// set the player place
		int playerNameTop = dealerCardTop + CARD_HEIGHT;
		int playerCardTop = playerNameTop + NAME_HEIGHT;
		for (int i = 0; i < numPlayer; i++) {
			namePlaces.add(new Label(messages.setID() + playerIds.get(i)));
			cardPlaces.add(new FlowPanel());
		}
		// set the player's place
		for (int i = 1; i < numPlayer + 1; i++) {
			backgroundArea.add(namePlaces.get(i));
			left = (int) (0.5 * (picWidth / numPlayer) - 0.5 * CARD_WIDTH + (i - 1)
					* (picWidth / numPlayer));
			backgroundArea.setWidgetPosition(namePlaces.get(i), left,
					playerNameTop);

			cardPlaces.get(i).setPixelSize(CARD_WIDTH * 3, CARD_HEIGHT);
			backgroundArea.add(cardPlaces.get(i));
			backgroundArea.setWidgetPosition(cardPlaces.get(i), left,
					playerCardTop);

		}

	
		//disable buttons
		restart.setEnabled(false);
		PairsPlusBtn.setEnabled(false);
		AnteBtn.setEnabled(false);
		PairsPlusAnteBtn.setEnabled(false);
		dealBtn.setEnabled(false);
		playBtn.setEnabled(false);
		foldBtn.setEnabled(false);
		save.setEnabled(false);
		load.setEnabled(false);
	    chipPairsPlus=0;
	    chipAnte=0;
         
	}
   /**
    * make card animation when click
    */
	@Override
	public void setAnimationCards(List<Card> dealerCards,
			List<ArrayList<Card>> desk) {
		// set dealer cards
		List<Image> dealerBackCardImages = createCardBackImages(dealerCards);
		placeInitiateImages(cardPlaces.get(0), dealerBackCardImages);
		// set player's cards
		for (int i = 0; i < desk.size(); i++) {
			List<Image> playerCardImages = createCardImages(desk.get(i));
			placeInitiateImages(cardPlaces.get(i + 1), playerCardImages);
		}
	}
    /**
     * set dealer and player cards
     */
	@Override
	public void setCards(List<Card> dealerCards, List<ArrayList<Card>> desk) {
		// set dealer cards
		List<Image> dealerBackCardImages = createCardBackImages(dealerCards);

		placeImages(cardPlaces.get(0), dealerBackCardImages);
		// set player's cards
		for (int i = 0; i < desk.size(); i++) {
			List<Image> playerCardImages = createCardImages(desk.get(i));
			placeImages(cardPlaces.get(i + 1), playerCardImages);
		}
	}
   /**
    * show dealer card with animation
    */
	@Override
	public void showDealerCards(List<Card> dealerCards) {
		List<Image> dealerCardImages = createCardImages(dealerCards);
		placeAnimationImages(cardPlaces.get(0), dealerCardImages);
	}
	/**
	    * show player card with animation
	    */
	@Override
	public void showPlayerCards(int i, List<Card> playerCards) {
		List<Image> playerCardImages = createCardImages(playerCards);
		placeAnimationImages(cardPlaces.get(i), playerCardImages);
	}

	private void placeImages(FlowPanel panel, List<Image> images) {
		panel.clear();
		Image last = images.isEmpty() ? null : images.get(images.size() - 1);
		for (Image image : images) {
			if (image != last) {
				// crop the image
				image.setPixelSize(20, 100);
			} else {
				// regular width
				image.setPixelSize(80, 100);
			}
			panel.add(image);
		}
	}
   /**
    * animation
    * @param panel
    * @param images
    */
	private void placeInitiateImages(FlowPanel panel, List<Image> images) {
		panel.clear();
		Image last = images.isEmpty() ? null : images.get(images.size() - 1);
		for (Image image : images) {
			if (image != last) {
				// crop the image
				animation = new CardMovingAnimation(panel, image, image, 20,
						100);
				animation.run(1000);
			} else {
				// regular width
				image.setPixelSize(80, 100);
				animation = new CardMovingAnimation(panel, image, image, 80,
						100);
				animation.run(1000);
			}
		}
	}

	private void placeAnimationImages(FlowPanel panel, List<Image> images) {
		panel.clear();
		Image last = images.isEmpty() ? null : images.get(images.size() - 1);
		for (Image image : images) {
			if (image != last) {
				image.setPixelSize(40, 100);
				animation = new CardMovingAnimation(panel, image, image, 40,
						100);
				animation.run(1000);
			} else {
				image.setPixelSize(60, 100);
				animation = new CardMovingAnimation(panel, image, image, 60,
						100);
				animation.run(1000);
			}
		}
	}

	private List<Image> createCardBackImages(List<Card> cards) {
		List<Image> res = new ArrayList<Image>();
		for (Card card : cards) {
			res.add(new Image(cardimages.getBackOfCardImage()));
		}
		return res;
	}

	private List<Image> createCardImages(List<Card> cards) {
		List<Image> res = new ArrayList<Image>();
		for (Card card : cards) {
			res.add(new Image(cardimages.getCardImage(card)));
		}
		return res;
	}

	@Override
	public void showStatus(String dealerStatus, ArrayList<String> playersStatus) {
		namePlaces.get(0).setText(dealerStatus);
		for (int i = 0; i < playersStatus.size(); i++) {
			namePlaces.get(i + 1).setText(playersStatus.get(i));
		}
	}
    /**
     * clear background, card places and name places
     */
	@Override
	public void clear() {
		backgroundArea.clear();
		cardPlaces.clear();
		namePlaces.clear();
	}
    /**
     * show balance
     */
	@Override
	public void showBalance1(int balance) {
		String balanceS = messages.setBalance1() + String.valueOf(balance);
		balance1.setText(balanceS);
	}

	@Override
	public void showBalance2(int balance) {
		String balanceS = messages.setBalance2() + String.valueOf(balance);
		balance2.setText(balanceS);
	}

	@Override
	public void showGameTurn(int turn, boolean b) {
		String turnInfo;
		if(b)
			 turnInfo= messages.setAIInfo();
		else	
		     turnInfo = messages.setNowPlayer() + String.valueOf(turn + 1)+ messages.setSTurn();
		gameTurn.setText(turnInfo);
	}
	
	@Override
	public void showConnect(String s){
		Connect.setText(s);
	}

	@Override
	public void showEmail(String s){
		Email.setText(messages.setEmail()+s);
	}
	
	@Override
	public void showRank(String r){
		rank.setText(r);
	}
	
	@Override
	public void showTurn(String s){
		Turn.setText(s);
	}
	
	@Override
	public void showNickName(String s){
		NickName.setText(messages.setNickName()+s);
	}
	
	@Override
	public void showOtherEmail(String s){
		OtherEmail.setText(messages.setOpEmail()+s);
	}
	
	@Override
	public void showOtherNickName(String s){
		OtherNickName.setText(messages.setOpNickName()+s);
	}
    
	
	@Override
	public void showGameInfo(int info) {
		String InfoS = null;
		if (info == 0) {
			InfoS = messages.setInfoS0();
		}
		if (info == 1 || info == 2) {
			InfoS = messages.setInfoS1();
		}
		if (info == 3 || info == 4) {
			InfoS = messages.setInfoS2();
		}
		if (info == 5) {
			InfoS = messages.setInfoS3();
		}
		gameInfo.setText(InfoS);
	}

	@Override
	public void showGameWarning(String warning) {
		gameWarning.setText(warning);
	}
	
	@Override
	public void showComputerChoose(String text){
		computerChoose.setText(text);
	}
	
	@Override
	public void showComputerChip(String text){
		computerChip.setText(text);
	}
	
	@Override
	public void showComputerDecision(String text){
		computerDecision.setText(text);
	}
	
    /**
     * below are sounds
     */
	@Override
	public void playChooseSound() {
		if (Audio.isSupported()) {
			chooseSound = Audio.createIfSupported();
			chooseSound.addSource(gameSounds.ChooseOgg().getSafeUri()
					.asString(), AudioElement.TYPE_OGG);
			chooseSound.addSource(gameSounds.ChooseMp3().getSafeUri()
					.asString(), AudioElement.TYPE_MP3);
			chooseSound.addSource(gameSounds.ChooseWav().getSafeUri()
					.asString(), AudioElement.TYPE_WAV);
			chooseSound.play();
		}
	}

	@Override
	public void playDecisionSound() {
		if (Audio.isSupported()) {
			decisionSound = Audio.createIfSupported();
			decisionSound.addSource(gameSounds.DecisionOgg().getSafeUri()
					.asString(), AudioElement.TYPE_OGG);
			decisionSound.addSource(gameSounds.DecisionMp3().getSafeUri()
					.asString(), AudioElement.TYPE_MP3);
			decisionSound.addSource(gameSounds.DecisionWav().getSafeUri()
					.asString(), AudioElement.TYPE_WAV);
			decisionSound.play();
		}
	}

	@Override
	public void playWarningSound() {
		if (Audio.isSupported()) {
			warningSound = Audio.createIfSupported();
			warningSound.addSource(gameSounds.WarningOgg().getSafeUri()
					.asString(), AudioElement.TYPE_OGG);
			warningSound.addSource(gameSounds.WarningMp3().getSafeUri()
					.asString(), AudioElement.TYPE_MP3);
			warningSound.addSource(gameSounds.WarningWav().getSafeUri()
					.asString(), AudioElement.TYPE_WAV);
			warningSound.play();
		}
	}
	/**
	 * below are about drag and drop
	 */
	 @Override
     public boolean isDragAndDropSupported() {
            return true;
     }
   
	@Override
	public void setNotDraggable(){		
		dragController.makeNotDraggable(chip10);
		dragController.makeNotDraggable(chip20);
		dragController.makeNotDraggable(chip50);
		dragController.makeNotDraggable(chip100);
		dragController.unregisterDropController(dropController);
	}
	
	@Override
	public void initializeDragging(DragHandler dragHandler) {
		dragController = new PickupDragController(backgroundArea, false);
		dragController.setBehaviorDragStartSensitivity(3);
		dragController.addDragHandler(dragHandler);
		dragController.makeDraggable(chip10);
		dragController.makeDraggable(chip20);
		dragController.makeDraggable(chip50);
		dragController.makeDraggable(chip100);
	}
	
	
	@Override
	public void initializeDropping() {
		dropController = new SimpleDropController(
				backgroundArea) {
			@Override
			public void onDrop(DragContext context) {
				super.onDrop(context);
				
			}

			@Override
			public void onPreviewDrop(DragContext context)
					throws VetoDragException {
				if(context.mouseX<=480&&context.mouseX>=380&&context.mouseY<=400&&context.mouseY>=300)
				{
					if(context.draggable==chip10){
						chipPairsPlus=10;
						Image chip=new Image(chipImages.chip10());
						chip.setPixelSize(60, 60);
						backgroundArea.add(chip);
						backgroundArea.setWidgetPosition(chip, 400,320);
					}
					else if(context.draggable==chip20){
						chipPairsPlus=20;
						Image chip=new Image(chipImages.chip20());
						chip.setPixelSize(60, 60);
						backgroundArea.add(chip);
						backgroundArea.setWidgetPosition(chip, 400,320);
					}
					else if(context.draggable==chip50){
						chipPairsPlus=50;
						Image chip=new Image(chipImages.chip50());
						chip.setPixelSize(60, 60);
						backgroundArea.add(chip);
						backgroundArea.setWidgetPosition(chip, 400,320);
					}
					else if(context.draggable==chip100){
						chipPairsPlus=100;
						Image chip=new Image(chipImages.chip100());
						chip.setPixelSize(60, 60);
						backgroundArea.add(chip);
						backgroundArea.setWidgetPosition(chip, 400,320);
					}
					save.setEnabled(false);
					PairsPlusBtn.setEnabled(true);
					if(chipAnte!=0){
					   PairsPlusAnteBtn.setEnabled(true);
					   PairsPlusBtn.setEnabled(false);
					   AnteBtn.setEnabled(false);					
					}					
					
				}
				
				if(context.mouseX<=600&&context.mouseX>=500&&context.mouseY<=400&&context.mouseY>=300)
				{
					if(context.draggable==chip10){
						chipAnte=10;
						Image chip=new Image(chipImages.chip10());
						chip.setPixelSize(60, 60);
						backgroundArea.add(chip);
						backgroundArea.setWidgetPosition(chip, 520,320);
					}
					else if(context.draggable==chip20){
						chipAnte=20;
						Image chip=new Image(chipImages.chip20());
						chip.setPixelSize(60, 60);
						backgroundArea.add(chip);
						backgroundArea.setWidgetPosition(chip, 520,320);
					}
					else if(context.draggable==chip50){
						chipAnte=50;
						Image chip=new Image(chipImages.chip50());
						chip.setPixelSize(60, 60);
						backgroundArea.add(chip);
						backgroundArea.setWidgetPosition(chip, 520,320);
					}
					else if(context.draggable==chip100){
						chipAnte=100;
						Image chip=new Image(chipImages.chip100());
						chip.setPixelSize(60, 60);
						backgroundArea.add(chip);
						backgroundArea.setWidgetPosition(chip, 520, 320);
					}
					save.setEnabled(false);
					AnteBtn.setEnabled(true);
					if(chipPairsPlus!=0){
					    PairsPlusAnteBtn.setEnabled(true);
					    AnteBtn.setEnabled(false);
					    PairsPlusBtn.setEnabled(false);
					}

				}
				throw new VetoDragException();

			}
		};
		dragController.registerDropController(dropController);
	}
	
	/**
	 * below are about buttons
	 */
	
	@Override
	public void setStartBtn(boolean b){
		  start.setEnabled(b);
		  
	}
	
	@Override
	public void setDealBtn(boolean b){
		  dealBtn.setEnabled(b);
		  
	}
	
	@Override
	public void setPlayBtn(boolean b){
		  playBtn.setEnabled(b);
		  
	}
	
	@Override
	public void setFoldBtn(boolean b){
		  foldBtn.setEnabled(b);

	}
	
	@Override
	public void setRestartBtn(boolean b){
		  restart.setEnabled(b);
	}
	
	@Override
	public void setSaveBtn(boolean b){
		  save.setEnabled(b);
	}
	
	@Override
	public void setLoadBtn(boolean b){
		  load.setEnabled(b);
	}
	
	@Override
	public void setCancelBtn(boolean b){
		cancel.setEnabled(b);
	}
	
	@Override
    public String getEmailInput() {
            return emailInput.getText();
    }
	
	

	@Override
	public void setSignOutLink(String Url){
		signOutLink.setHref(Url);
	}
	
	@Override
    public void setMatchList(String match) {
            matchList.addItem(match);
    }

    @Override
    public void clearMatchList() {
            matchList.clear();
    }	
    
    @Override
    public String getSelectionFromMatchList() {
            int idx = matchList.getSelectedIndex();
            if (idx == -1)
                    return null;
            return matchList.getItemText(idx);
    }

		
}
