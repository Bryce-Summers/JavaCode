package Project;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import util.ImageUtil;

import BryceImages.Operations.Drawing;
import BryceImages.Operations.ImageFactory;
import BryceImages.Rendering.ColorCalculator;
import BryceImages.Rendering.StartRender;
import BryceMath.Calculations.Colors;
import FrameSpecifications.ccDinnerTableItem;
import FrameSpecifications.ccNegativeFeedback;
import FrameSpecifications.ccPerlinBackground;
import FrameSpecifications.ccTrainCar;
import Game_Engine.Engine.Objs.ImageB;
import Game_Engine.Engine.Objs.Room;
import Game_Engine.Engine.engine.Game_output;
import Game_Engine.GUI.Components.small.gui_label;
import TrainTracks.ccHumpYardTrack;

/*
 * CardFrame rendering room.
 */

public class room_main extends Room
{
	// -- Private variables.
	
	// -- Constructor.
	public room_main(Game_output out)
	{
		super(out);
	}

	@Override
	public void iObjs()
	{
		// The dimensions for cards.
		int w = 825;
		int h = 1125;
	
		//NegativeFeedBack(w, h);
		PositiveFeedback(w, h);
		
		/*
		String[] names = {"Bowl", "Bread Plate", "Butter Knife", "Fork", "Glass", "Knife", "Napkin", "Placemat", "Placemat", "Plate", "Spoon"};
		//DinnerTableGameItem(w, h, names);
		String[] meal_names = {
				"Apple Sauce", "Requires: Bowl, Spoon",
				"Cheese and Crackers", "Requires: Bread Plate, Knife",
				"Chicken Nuggets with Condiments", "Requires: Napkin, Plate, Bowl",
				"Corn on the Cobb", "Requires: Plate, Butter Knife",
				"Ice Cream Float", "Requires: Glass, Spoon",
				"Mash Potatoes", "Requires: Plate, Fork, Knife, ButterKnife",
				"Ribs", "Requires: Napkin, Dinner Plate, Knife",
				"Rice", "Requires: Fork, Plate or Bowl",
				"Rolls", "Requires: Bread Plate, Butter Knife, Napkin",
				"Soup and Crackers", "Requires: Bread Plate, Bowl, Spoon",
				"Turkey", "Requires: Fork, Plate, Knife",
				"Water", "Requires: Glass",
				"Wine",  "Requires: Glass"
		};
		DinnerTableGameMeal(h, w, meal_names);
		
		// Meal Back.
		//DinnerTableGameMealBack(h, w, "Meal", 41, .25);
		
		// Item back.
		//DinnerTableGameMealBack(w, h, "Item", 197, .16);
		 */
		
		
		//TrainTracks(675, 675);
		//TrainCars(w, h);
		//TrainGoals(h, w);
	}
	
	private void TrainCars(int w, int h)
	{
		// TODO Auto-generated method stub
		StartRender R = new StartRender(true);
		
		System.out.println("Started rendering cars.");
		
		
		ccPerlinBackground cc = new ccPerlinBackground(w, h, 0, 0);
		BufferedImage background = R.render(cc);
		
		BufferedImage[] cars = carImages(R, w/2, w/2/2);
		int len = cars.length;
		
		for(int i = 0; i < len; i++)
		{
			BufferedImage output = ImageFactory.blank(w, h);
			Graphics g = output.getGraphics();
			
			g.drawImage(background, 0, 0, null);
			Drawing.draw_scaled(output, cars[i], 1.0, false);
			
			// Save the image to the file.
			ImageUtil.saveImage(output, "Car_" + i);
		}
		
		// -- Render the back face.
		BufferedImage output = ImageFactory.blank(w, h);
		Graphics g = output.getGraphics();
		g.drawImage(background, 0, 0, null);
		
		gui_label l = new gui_label(0, 0, w, h);
		l.setText("Cars");
		l.setTextSize(h/8);
		ImageB i = new ImageB(output);
		AffineTransform AT = new AffineTransform();
		l.draw(i, AT);
		ImageUtil.saveImage(output, "back");
		
		System.out.println("Done rendering Train cars.");
	}
	
	private void TrainGoals(int w, int h)
	{
		// TODO Auto-generated method stub
		StartRender R = new StartRender(true);
		
		System.out.println("Started rendering train goals");
		
		
		ccPerlinBackground cc = new ccPerlinBackground(w, h, 190, 50);
		BufferedImage background = R.render(cc);
		
		int car_w = w/2*11/36;
		int car_h = w/2*11/36/2;
		BufferedImage[] cars = carImages(R, car_w, car_h);
		
		// Used to create separations between the cars.
		car_w = car_w*11/10;
		
		/*
		// Iterate through all possible shipment goals.
		for(int c0 = 0; c0 < 2; c0++)
		for(int c1 = 0; c1 < 2; c1++)
		for(int c2 = 0; c2 < 2; c2++)
		for(int c3 = 0; c3 < 2; c3++)
		for(int c4 = 0; c4 < 2; c4++)
		for(int pos_start = 0; pos_start < 5; pos_start++)
		{
			int len = c0 + c1 + c2 + c3 + c4;
			if(pos_start >= len)
			{
				continue;
			}
			
			int pos = pos_start;
			
			BufferedImage output = ImageFactory.blank(w, h);
			Graphics g = output.getGraphics();
			
			g.drawImage(background, 0, 0, null);
			
			if(c0 == 1)
			{
				g.drawImage(cars[0], w/2 - len*car_w/2 + pos*car_w, h/2 - car_h/2, null);
				pos = (pos + 1) % len;
			}
			
			if(c1 == 1)
			{
				g.drawImage(cars[1], w/2 - len*car_w/2 + pos*car_w, h/2 - car_h/2, null);
				pos = (pos + 1) % len;
			}
			
			if(c2 == 1)
			{
				g.drawImage(cars[2], w/2 - len*car_w/2 + pos*car_w, h/2 - car_h/2, null);
				pos = (pos + 1) % len;
			}
			
			
			if(c3 == 1)
			{
				g.drawImage(cars[3], w/2 - len*car_w/2 + pos*car_w, h/2 - car_h/2, null);
				pos = (pos + 1) % len;
			}
			
			if(c4 == 1)
			{
				g.drawImage(cars[4], w/2 - len*car_w/2  + pos*car_w, h/2 - car_h/2, null);
				pos = (pos + 1) % len;
			}
			
					
			// Save the image to the file.
			ImageUtil.saveImage(output, "Goal_" + c0 + c1 + c2 + c3 + c4 + pos_start);
		}*/
		
		// -- Render the back face.
		BufferedImage output = ImageFactory.blank(w, h);
		Graphics g = output.getGraphics();
		g.drawImage(background, 0, 0, null);
		
		gui_label l = new gui_label(0, 0, w, h);
		l.setText("Goals");
		l.setTextSize(h/4);
		ImageB i = new ImageB(output);
		AffineTransform AT = new AffineTransform();
		l.draw(i, AT);
		ImageUtil.saveImage(output, "back");
		
		System.out.println("Done rendering Train goals.");
	}
	
	// Renders a set of rendered car Images.
	BufferedImage[] carImages(StartRender R, int w, int h)
	{
		BufferedImage[] cars = new BufferedImage[5];
		
		// Red
		cars[0] = R.render(new ccTrainCar(w, h, 0, 100));
		
		// White.
		cars[1] = R.render(new ccTrainCar(w, h, 0, 0));
		
		// Purple.
		cars[2] = R.render(new ccTrainCar(w, h, 282, 100));
		
		// Yellow
		cars[3] = R.render(new ccTrainCar(w, h, 48, 68));
		
		// Black.
		cars[4] = R.render(new ccTrainCar(w, h, 0, 0, 25));
		
		return cars;
	}

	private void TrainTracks(int w, int h)
	{
		// TODO Auto-generated method stub
		StartRender R = new StartRender(true);
		
		boolean[] vals = {true, false};
		
		int name = 0;
		

		
		for(boolean c1 : vals)
		for(boolean c2 : vals)
		for(boolean c3 : vals)
		for(boolean c4 : vals)
		for(boolean s1 : vals)
		for(boolean s2 : vals)
		{
			ColorCalculator cc = new ccHumpYardTrack(w, h, c1, c2, c3, c4, s1, s2);
			BufferedImage image = R.render(cc);
			
			BufferedImage background = ImageFactory.ColorRect(Color.WHITE, w, h);
			Graphics g = background.getGraphics();
			g.drawImage(image, 0, 0, null);
			
			ImageUtil.saveImage(background, "Track_" + name);
			name++;
		}
		
		
		System.out.println("Done rendering Train Tracks.");
	}

	public void DinnerTableGameItem(int w, int h, String[] names)
	{
		StartRender R = new StartRender(true);
		ColorCalculator cc = new ccDinnerTableItem(w, h, 25);
		BufferedImage background = R.render(cc);
		
		ImageDisplay display = new ImageDisplay(0, 0, w, h);
		
		for(String name : names)
		{
			RenderItemCard(display.getSprite(), background, name);
			ImageUtil.saveImage(display.getSprite(), name);
		}
		
		obj_create(display);
		
		System.out.println("Done");
	}
	
	public void DinnerTableGameMeal(int w, int h, String[] names)
	{
		StartRender R = new StartRender(true);
		ColorCalculator cc = new ccDinnerTableItem(w, h, 25);
		BufferedImage background = R.render(cc);
		
		ImageDisplay display = new ImageDisplay(0, 0, w, h);
		
		
		int len = names.length/2;
		for(int i = 0; i < len; i++)
		{
			RenderMealCard(display.getSprite(), background, names[i*2], names[i*2 + 1]);
			ImageUtil.saveImage(display.getSprite(), names[i*2]);
		}
		
		obj_create(display);
		
		System.out.println("Done");
	}
	
	public void DinnerTableGameMealBack(int w, int h, String name, int hue, double font_size)
	{
		StartRender R = new StartRender(true);
		ColorCalculator cc = new ccDinnerTableItem(w, h, 25, 100, hue);
		BufferedImage background = R.render(cc);
		
		Graphics2D g = (Graphics2D) background.getGraphics();
		
		AffineTransform AT = new AffineTransform();
		
		gui_label l = new gui_label(0, 0, w, h);
		l.setText(name);
		l.setTextSize((int)(h * font_size));
		l.draw(new ImageB(background), AT);
		
		ImageUtil.saveImage(background, "CardBack");
		
		
		//obj_create(display);
		
		System.out.println("Done");		
	}

	public void NegativeFeedBack(int w, int h)
	{
		
		StartRender R = new StartRender(true);
		ColorCalculator cc = new ccNegativeFeedback(w, h, 25);
		BufferedImage background = R.render(cc);
		
		ImageDisplay display = new ImageDisplay(0, 0, w, h);
		
		// The cards.
		String info_text = "2nd Edition, Copyright 2015 Bryce Summers";
		renderNegativeFeedbackImage(display.getSprite(), background, "Take 1",
				"flavor text", "More flavor text", info_text,
				"Take 1 card at random from any player's hand.");
		ImageUtil.saveImage(display.getSprite(), "card1");
		
		renderNegativeFeedbackImage(display.getSprite(), background, "Choose 1",
			"Beggars cannot be choosers.", "It is a good thing I am no beggar.",
			info_text, "Look at another player's hand.", "Choose and take 1 card from it.");
		ImageUtil.saveImage(display.getSprite(), "card2");
		
		renderNegativeFeedbackImage(display.getSprite(), background, "Choose 2",
			"When you cannot decide...", "...just take them both.",
			info_text, "Look at another player's hand.", "Choose and take 2 cards from it.");
			ImageUtil.saveImage(display.getSprite(), "card3");
			
		renderNegativeFeedbackImage(display.getSprite(), background, "Draw 1",
			"If I cannot win and cannot lose then I must draw.", "",
			info_text, "Draw 1 card from the top of the deck.");
			ImageUtil.saveImage(display.getSprite(), "card4");
		
		renderNegativeFeedbackImage(display.getSprite(), background, "Draw 2, Drain 2",
			"Your loss is my gain!", "",
			info_text, "Draw 2 cards from the top of the deck.", "Select another player.", "Randomly discard 2 cards from their hand.");
			ImageUtil.saveImage(display.getSprite(), "card5");
			
		renderNegativeFeedbackImage(display.getSprite(), background, "Draw 1, Take 2",
			"One from the deck is worth two from the hand.", "",
			info_text, "Draw 1 card from the top of the deck.", "Take 2 cards at random from any player's hand.");
			ImageUtil.saveImage(display.getSprite(), "card6");	
			
		renderNegativeFeedbackImage(display.getSprite(), background, "Take 2, Discard 1",
			"I will not keep this card, because", "I only keep the ones that I like. ~Prodigal Son",
			info_text, "Take 2 cards at random from any player's hand.", "Discard 1 card from your hand.");
			ImageUtil.saveImage(display.getSprite(), "card7");
			
		renderNegativeFeedbackImage(display.getSprite(), background, "Draw 1, Cycle 1",	
			"I drew. I took. I lost part of my hand. Memoirs of", "Julius Caesar, purveyor of three word statements.",
			info_text, "Draw 1 card from the top of the deck.", "Take 1 card at random from any player's hand.", "Discard 1 card from your hand.");
			ImageUtil.saveImage(display.getSprite(), "card8");
		
		renderNegativeFeedbackImage(display.getSprite(), background, "Exchange Hands",	
			"An eye for an ery, a hand for a hand.", "That is my handy solution to this handsome problem.",
			info_text, "Exchange hands with another player.");
			ImageUtil.saveImage(display.getSprite(), "card9");
			
		renderNegativeFeedbackImage(display.getSprite(), background, "Do Nothing",	
			"Some people win by doing nothing.", "Nothing is better than this card!",
			info_text, "Relax and pass the turn.");
			ImageUtil.saveImage(display.getSprite(), "card10");
		
		renderNegativeFeedbackImage(display.getSprite(), background, "Rotate Hands Left",	
			"As I drew my last card,", "my hand became all that was left.",
			info_text, "Draw 1 card from the top of the deck.", "Player gives their hands to their left neighbor.");
			ImageUtil.saveImage(display.getSprite(), "card11");
			
		renderNegativeFeedbackImage(display.getSprite(), background, "Rotate Hands Right",	
			"As I lay starving, I drew my last courage and,", "did the right thing, taking all that was left.",
			info_text, "Draw 1 card from the top of the deck.", "Player gives their hands to their right neighbor.");
			ImageUtil.saveImage(display.getSprite(), "card12");
		
		// Take cards.
		renderNegativeFeedbackImage(display.getSprite(), background, "Take 1",	
			"It takes one to know one said the kettle to the pot.", "",
			info_text, "Take 1 card at random from any player's hand.");
			ImageUtil.saveImage(display.getSprite(), "take1A");
			

		renderNegativeFeedbackImage(display.getSprite(), background, "Take 1",	
				"Give it to me! I can take it! -Famous last words.", "",
				info_text, "Take 1 card at random from any player's hand.");
		ImageUtil.saveImage(display.getSprite(), "take1B");
		
		renderNegativeFeedbackImage(display.getSprite(), background, "Take 2",	
			"It takes two to tango together Tom thought", "too truefully.",
			info_text, "Take 2 cards at random from any player's hand.");
		ImageUtil.saveImage(display.getSprite(), "take2A");
		
		renderNegativeFeedbackImage(display.getSprite(), background, "Take 2",	
			"Penny to the pile for your thoughts.", "Now give me your two cents!",
			info_text, "Take 2 cards at random from any player's hand.");
		ImageUtil.saveImage(display.getSprite(), "take2B");
		
		renderNegativeFeedbackImage(display.getSprite(), background, "Take 3",	
				"When two is simply not enough.", "",
				info_text, "Take 3 cards at random from any player's hand.");
		ImageUtil.saveImage(display.getSprite(), "take3A");
		
		renderNegativeFeedbackImage(display.getSprite(), background, "Take 3",	
			"I wish I had an exchange hands instead.", "The person on his left just smiled.",
			info_text, "Take 3 cards at random from any player's hand.");
		ImageUtil.saveImage(display.getSprite(), "take3B");
			
		renderNegativeFeedbackImage(display.getSprite(), background, "Take 4",	
			"1-2-3-4!", "I cannot take it anymore!",
			info_text, "Take 4 cards at random from any player's hand.");
		ImageUtil.saveImage(display.getSprite(), "take4");
		
		renderNegativeFeedbackImage(display.getSprite(), background, "Take 5 total, Discard 3",	
			"One from one of you.", "Two from two of you.",
			info_text, "Take 5 cards at random from other player's hands.", "The cards may come from different hands.", "Discard 3 cards.");
			ImageUtil.saveImage(display.getSprite(), "take5");
			
		renderNegativeFeedbackImage(display.getSprite(), background, "Garbage Collection",	
			"The trash of one person is the treasure of another.", "This card had better be good!",
			info_text, "Take the top card of the discard pile.", "Add the card to your hand.", "Place this card on top of the discard pile.");
			ImageUtil.saveImage(display.getSprite(), "GarbageCollection");
			
		// Just for show at the end.
		obj_create(display);
		
		System.out.println("Done");
		
	}
	
	public void PositiveFeedback(int w, int h)
	{
		
		StartRender R = new StartRender(true);
		ColorCalculator cc = new ccNegativeFeedback(w, h, 25);
		BufferedImage background = R.render(cc);
		
		ImageDisplay display = new ImageDisplay(0, 0, w, h);
		
		// The cards.
		String info_text = "1st Edition, Copyright 2015 Bryce Summers";
		renderNegativeFeedbackImage(display.getSprite(), background, "Mind Swap",
				"A lifetime of learning. Stolen with a single spell.", "", info_text,
				"Exchange hands with an opponent.");
		ImageUtil.saveImage(display.getSprite(), "Mind Swap");
		
		renderNegativeFeedbackImage(display.getSprite(), background, "Left Current",
			"Modern wires currently prefer left over right.", "",
			info_text, "Draw 1 card from the deck.", "Players pass their hands to their left.");
		ImageUtil.saveImage(display.getSprite(), "Left Current");
		
		renderNegativeFeedbackImage(display.getSprite(), background, "Right Current",
				"Old school wires preferred right over left.", "",
				info_text, "Draw 1 card from the deck.", "Players pass their hands to their right.");
		ImageUtil.saveImage(display.getSprite(), "Right Current");
		
		String name = "Short Circuit";
		renderNegativeFeedbackImage(display.getSprite(), background, name,
				"End of the line.", "",
				info_text, "(Reactive) Block a \"take\" action.");
		ImageUtil.saveImage(display.getSprite(), name);
		
		name = "Surge Protector";
		renderNegativeFeedbackImage(display.getSprite(), background, name,
				"Protection that packs a punch.", "",
				info_text, "(Reactive) Block a \"take\" action.", "Take 1 card from any opponent.");
		ImageUtil.saveImage(display.getSprite(), name);
		
		name = "Transformer";
		renderNegativeFeedbackImage(display.getSprite(), background, name,
				"Good electramancers learn from", "every attack.",
				info_text, "(Reactive) Block a \"take\" action.", "Draw 2 cards from the deck.");
		ImageUtil.saveImage(display.getSprite(), name);
		
		name = "Super Drain";
		renderNegativeFeedbackImage(display.getSprite(), background, name,
				"Highly dangerous. Potentially deadly.", "",
				info_text, "Take all cards from any opponent.");
		ImageUtil.saveImage(display.getSprite(), name);
		
		name = "Terra Drain";
		renderNegativeFeedbackImage(display.getSprite(), background, name,
				"The latest attack discovered though", "4 years of shocking research.",
				info_text, "Take 4 cards from any opponent.");
		ImageUtil.saveImage(display.getSprite(), name);

		name = "Giga Drain";
		renderNegativeFeedbackImage(display.getSprite(), background, name,
				"Attack, Attack!, ATTACK!!", "",
				info_text, "Take 3 cards from any opponent.");
		ImageUtil.saveImage(display.getSprite(), name);
					
		name = "Mega Drain";
		renderNegativeFeedbackImage(display.getSprite(), background, name,
				"Commodity electramancer attack.", "",
				info_text, "Take 2 cards from any opponent.");
		ImageUtil.saveImage(display.getSprite(), name);
		
		name = "Kilo Drain";
		renderNegativeFeedbackImage(display.getSprite(), background, name,
				"Wouldn't hurt a fly.", "",
				info_text, "Take 1 card from any opponent.");
		ImageUtil.saveImage(display.getSprite(), name);
		
		name = "Distributed Attack";
		renderNegativeFeedbackImage(display.getSprite(), background, name,
				"Social Electramancers share", "their attacks with many friends.",
				info_text, "Take 1 card from any opponent.", "Take 1 card from any opponent.", "Take 1 card from any opponent.");
		ImageUtil.saveImage(display.getSprite(), name);
		
		name = "Giga Battery";
		renderNegativeFeedbackImage(display.getSprite(), background, name,
				"Sturdy construction.", "Holds charge for 3 years.",
				info_text, "Draw 3 cards from the deck.");
		ImageUtil.saveImage(display.getSprite(), name);
		
		name = "Mega Battery";
		renderNegativeFeedbackImage(display.getSprite(), background, name,
				"Useful in dark alleyways.", "",
				info_text, "Draw 2 cards from the deck.");
		ImageUtil.saveImage(display.getSprite(), name);
		
		name = "Recycle";
		renderNegativeFeedbackImage(display.getSprite(), background, name,
				"The scavenger gets the last laugh.", "",
				info_text, "Pickup the last card played before", "this one from the discard pile.");
		ImageUtil.saveImage(display.getSprite(), name);
		
		name = "Firewall";
		renderNegativeFeedbackImage(display.getSprite(), background, name,
				"One never can be too careful.", "",
				info_text, "Select 1 card from your hand.", "Add the card to your hidden hand.");
		ImageUtil.saveImage(display.getSprite(), name);
		
		name = "Remote Server";
		renderNegativeFeedbackImage(display.getSprite(), background, name,
				"Double the savings.", "",
				info_text, "Select 2 cards from your hand.", "Add the cards to your hidden hand.");
		ImageUtil.saveImage(display.getSprite(), name);
		
		name = "Giga Transfer";
		renderNegativeFeedbackImage(display.getSprite(), background, name,
				"The energy was reported lost", "due to three dogs at night.",
				info_text, "Give 3 cards to any opponent.");
		ImageUtil.saveImage(display.getSprite(), name);
		
		name = "Mega Transfer";
		renderNegativeFeedbackImage(display.getSprite(), background, name,
				"Two can be as bad as one.", "",
				info_text, "Give 2 cards to any opponent.");
		ImageUtil.saveImage(display.getSprite(), name);
		
		name = "Kilo Transfer";
		renderNegativeFeedbackImage(display.getSprite(), background, name,
				"One is the loneliest number.", "",
				info_text, "Give 1 cards to any opponent.");
		ImageUtil.saveImage(display.getSprite(), name);
		
		name = "Shock";
		renderNegativeFeedbackImage(display.getSprite(), background, name,
				"Just a little feedback.", "",
				info_text, "Take 2 cards from any opponent.", "Give 1 card to the same opponent.");
		ImageUtil.saveImage(display.getSprite(), name);
		
		name = "Ping";
		renderNegativeFeedbackImage(display.getSprite(), background, name,
				"Take and learn.", "",
				info_text, "Take 1 card from any opponent.", "Draw 1 card form the deck.");
		ImageUtil.saveImage(display.getSprite(), name);
		
		name = "Stall";
		renderNegativeFeedbackImage(display.getSprite(), background, name,
				"Relax.", "",
				info_text, "Draw 1 card form the deck.");
		ImageUtil.saveImage(display.getSprite(), name);
		
		name = "Super Sink";
		renderNegativeFeedbackImage(display.getSprite(), background, name,
				"Combo time...", "",
				info_text, "Discard 2 cards.", "Discard 1 card.", "Draw 3 cards from the deck.");
		ImageUtil.saveImage(display.getSprite(), name);
		
		name = "Mega Sink";
		renderNegativeFeedbackImage(display.getSprite(), background, name,
				"Bandwidth limited.", "",
				info_text, "Draw 2 cards from the deck.", "Take 1 card from any opponent.", "Discard 2 cards.");
		ImageUtil.saveImage(display.getSprite(), name);
		
		name = "Kilo Sink";
		renderNegativeFeedbackImage(display.getSprite(), background, name,
				"Sinks have good throughput.", "",
				info_text, "Draw 1 card from the deck.", "Take 1 card from any opponent.", "Discard 1 card.");
		ImageUtil.saveImage(display.getSprite(), name);

		name = "Mega Search";
		renderNegativeFeedbackImage(display.getSprite(), background, name,
				"Some electramancers are not", "bound by random chance.",
				info_text, "Look any opponent's hand.", "Choose and take 2 cards from it.");
		ImageUtil.saveImage(display.getSprite(), name);
		
		name = "Kilo Search";
		renderNegativeFeedbackImage(display.getSprite(), background, name,
				"Beyond lucky!", "",
				info_text, "Look any opponent's hand.", "Choose and take 1 card from it.");
		ImageUtil.saveImage(display.getSprite(), name);

				
		// Just for show at the end.
		obj_create(display);
		
		System.out.println("Done");
		
	}
	
	public void renderNegativeFeedbackImage(BufferedImage image, BufferedImage background, String title_text, String flavor_text, String flavor_text2, String game_text, String ... Rules_text)
	{
		
		ImageB imageb = new ImageB(image);
		AffineTransform AT = new AffineTransform();
		
		int w = image.getWidth();
		int h = image.getHeight();
		
		Graphics2D g = (Graphics2D) image.getGraphics();
		
		g.drawImage(background, 0, 0, null);
		
		int border_size = 25;
		int left = border_size + 24;
		int right = w - border_size - 24;
		
		// The title bar.
		gui_label title = new gui_label(left, border_size + 24, right - left, 48*2);
		title.setDrawBorders(true);
		title.setColor(Colors.Color_hsv(200, 70, 100, 40));
		title.setTextSize(48);
		title.setText(title_text);
		title.draw(imageb, AT);

		// The picture location.
		gui_label picture = new gui_label(left, title.getY2() + border_size, right - left, 500);
		picture.setDrawBorders(true);
		//picture.setColor(Colors.Color_hsv(108, 70, 100, 40));
		picture.setColor(Color.WHITE);
		picture.draw(imageb, AT);
		
		// The rules box location.
		gui_label rules = new gui_label(picture.getX(), picture.getY2() + border_size, picture.getW(), 200);
		rules.setDrawBorders(true);
		rules.setColor(Colors.Color_hsv(0, 0, 100, 80));
		rules.draw(imageb, AT);
		
		int bullet_y = (int) (rules.getY() + 48);
		
		for(String Rule: Rules_text)
		{
			drawTextLeft(g, AT, rules.getX() + 24, bullet_y, "\\cdot " + Rule);
			bullet_y += 48;
		}
		
		
		gui_label flavor = new gui_label(picture.getX(), rules.getY2() + border_size, picture.getW(), 100);
		flavor.setDrawBorders(true);
		flavor.setColor(Colors.Color_hsv(108, 70, 100, 40));
		//flavor.setText(flavor_text);
		flavor.draw(imageb, AT);
		if(flavor_text2.equals(""))
		{
			flavor.setText(flavor_text);
			flavor.draw(imageb, AT);
		}
		else
		{
			flavor.draw(imageb, AT);
			drawTextLeft(g, AT, flavor.getX() + 24, flavor.getY() + 24, flavor_text);
			drawTextLeft(g, AT, flavor.getX() + 24, flavor.getY() + 24 + 48, flavor_text2);
		}
		
		
		
		gui_label info = new gui_label(rules.getX(), flavor.getY2(), picture.getW(), h - border_size - (int)flavor.getY2());
		//info.setDrawBorders(true);
		//info.setColor(Colors.Color_hsv(108, 70, 100, 40));
		info.setText(game_text);
		info.setTextSize(16);
		info.draw(imageb, AT);
		
		
		g.dispose();
	}
	
	public void RenderItemCard(BufferedImage image, BufferedImage background, String title_text)			
	{
		
		ImageB imageb = new ImageB(image);
		AffineTransform AT = new AffineTransform();
		
		int w = image.getWidth();
		int h = image.getHeight();
		
		Graphics2D g = (Graphics2D) image.getGraphics();
		
		g.drawImage(background, 0, 0, null);
		
		int border_size = 25;
		int left = border_size + 24;
		int right = w - border_size - 24;
		
		// The title bar.
		gui_label title = new gui_label(left, border_size + 24, right - left, 48*2);
		title.setDrawBorders(true);
		title.setColor(Colors.Color_hsv(200, 100, 100));
		title.setTextSize(48);
		title.setText(title_text);
		title.draw(imageb, AT);

		// The picture location.
		gui_label picture = new gui_label(left, title.getY2() + border_size, right - left, (int) (h - border_size*2 - (title.getY2() + border_size)));
		picture.setDrawBorders(true);
		picture.setColor(Colors.Color_hsv(108, 70, 100, 40));
		picture.draw(imageb, AT);
	}
	
	public void RenderMealCard(BufferedImage image, BufferedImage background, String title_text, String requirements)			
	{
		
		ImageB imageb = new ImageB(image);
		AffineTransform AT = new AffineTransform();
		
		int w = image.getWidth();
		int h = image.getHeight();
		
		Graphics2D g = (Graphics2D) image.getGraphics();
		
		g.drawImage(background, 0, 0, null);
		
		int border_size = 25;
		int left = border_size + 24;
		int right = w - border_size - 24;
		
		// The title bar.
		gui_label title = new gui_label(left, border_size + 24, right - left, 48*2);
		title.setDrawBorders(true);
		title.setColor(Colors.Color_hsv(149, 20, 100));
		title.setTextSize(48);
		title.setText(title_text);
		title.draw(imageb, AT);

		// The picture location.
		gui_label picture = new gui_label(left, title.getY2() + border_size, right - left, (int) (h - border_size*2 - (title.getY2() + border_size) - 100));
		picture.setDrawBorders(true);
		picture.setColor(Colors.Color_hsv(108, 5, 49, 80));
		picture.draw(imageb, AT);
		
		
		// The rules box location.
		gui_label rules = new gui_label(picture.getX(), picture.getY2() + border_size, picture.getW(), 100 - border_size);
		rules.setDrawBorders(true);
		rules.setColor(Colors.Color_hsv(149, 20, 100));
		rules.setText(requirements);
		rules.draw(imageb, AT);
		
		
		
		
	}
	
	@Override
	public void update()
	{
		super.update();
	}

}