package com.lilithsthrone.game.dialogue.places.submission;

import com.lilithsthrone.game.character.GameCharacter;
import com.lilithsthrone.game.character.npc.NPC;
import com.lilithsthrone.game.dialogue.DialogueNodeOld;
import com.lilithsthrone.game.dialogue.responses.Response;
import com.lilithsthrone.game.dialogue.responses.ResponseEffectsOnly;
import com.lilithsthrone.game.dialogue.utils.UtilText;
import com.lilithsthrone.main.Main;
import com.lilithsthrone.utils.Vector2i;
import com.lilithsthrone.world.WorldType;
import com.lilithsthrone.world.places.PlaceType;

/**
 * @since 0.1.0
 * @version 0.2.2
 * @author Innoxia
 */
public class SubmissionGenericPlaces {

	public static final DialogueNodeOld WALKWAYS = new DialogueNodeOld("Walkways", "", false) {
		private static final long serialVersionUID = 1L;
		
		@Override
		public String getAuthor() {
			return "Duner & Innoxia";
		}
		
		@Override
		public int getMinutesPassed(){
			return 5;
		}
		
		@Override
		public String getContent() {
			return UtilText.parseFromXMLFile("places/submission/submissionPlaces", "WALKWAYS")
					+ (Math.random()<0.2f
							?UtilText.parseFromXMLFile("places/submission/submissionPlaces", "WALKWAYS_EXTRA")
							:"");
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			return null;
		}
	};
	
	public static final DialogueNodeOld TUNNEL = new DialogueNodeOld("Tunnels", "", false) {
		private static final long serialVersionUID = 1L;

		@Override
		public String getAuthor() {
			return "Duner";
		}

		@Override
		public int getMinutesPassed(){
			return 5;
		}
		
		@Override
		public String getContent() {
			UtilText.nodeContentSB.setLength(0);
			UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/submissionPlaces", "TUNNEL"));
			
			for(GameCharacter npc : Main.game.getCharactersPresent()) {
				UtilText.nodeContentSB.append(((NPC) npc).getPresentInTileDescription());
			}
			
			return UtilText.nodeContentSB.toString();
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			if(index == 6) {
				return new ResponseEffectsOnly(
						"Explore",
						"Explore the tunnels. Although you don't think you're any more or less likely to find anything by doing this, at least you won't have to keep travelling back and forth..."){
							@Override
							public void effects() {
								DialogueNodeOld dn = Main.game.getActiveWorld().getCell(Main.game.getPlayer().getLocation()).getPlace().getDialogue(true);
								Main.game.setContent(new Response("", "", dn));
							}
						};
			} else {
				return null;
			}
		}
	};

	public static final DialogueNodeOld BAT_CAVERNS = new DialogueNodeOld("Bat Caverns", "", false) {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed(){
			return 5;
		}
		
		@Override
		public String getContent() {
			return UtilText.parseFromXMLFile("places/submission/submissionPlaces", "BAT_CAVERNS");
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			if (index == 1) {
				return new Response("Bat Caverns", "Enter the bat caverns. <b>Not yet added!</b> (This will be a mini-area, which will contain a couple of side-quests.)", null);

			} else {
				return null;
			}
		}
	};
	
	public static final DialogueNodeOld RAT_WARREN = new DialogueNodeOld("The Rat Warren", "", false) {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed(){
			return 5;
		}
		
		@Override
		public String getContent() {
			return UtilText.parseFromXMLFile("places/submission/submissionPlaces", "RAT_WARREN");
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			if (index == 1) {
				return new Response("Knock", "Knock on the door. <b>Not yet added!</b> (This will be a mini-area, which will be related to a large side-quest.)", null);

			} else {
				return null;
			}
		}
	};

	public static final DialogueNodeOld GAMBLING_DEN = new DialogueNodeOld("Gambling Den", "", false) {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed(){
			return 5;
		}
		
		@Override
		public String getContent() {
			return UtilText.parseFromXMLFile("places/submission/submissionPlaces", "GAMBLING_DEN");
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			if (index == 1) {
				return new Response("Gambling Den", "Enter the Gambling Den. <b>Not yet added!</b>", null);

			} else {
				return null;
			}
		}
	};

	public static final DialogueNodeOld LILIN_PALACE_CAVERN = new DialogueNodeOld("Cavern", "", false) {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed(){
			return 5;
		}
		
		@Override
		public String getContent() {
			return UtilText.parseFromXMLFile("places/submission/submissionPlaces", "LILIN_PALACE_CAVERN");
		}


		@Override
		public Response getResponse(int responseTab, int index) {
			return null;
		}
	};
	
	public static final DialogueNodeOld LILIN_PALACE_GATE = new DialogueNodeOld("Lyssieth's Palace Gate", "", true) {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed(){
			return 5;
		}
		
		@Override
		public String getContent() {
			return UtilText.parseFromXMLFile("places/submission/submissionPlaces", "LILIN_PALACE_GATE");
		}


		@Override
		public Response getResponse(int responseTab, int index) {
			if (index == 1) {
				return new ResponseEffectsOnly("Step back", "Do as the guard says and step back.") {
					@Override
					public void effects() {
						Main.game.getPlayer().setLocation(new Vector2i(Main.game.getPlayer().getLocation().getX(), Main.game.getPlayer().getLocation().getY()-1));
						Main.game.setContent(new Response("", "", LILIN_PALACE_CAVERN));
					}
				};

			} else if (index == 2) {
				return new Response("Uniforms", "Ask Elizabeth why she and her troops are wearing Victorian uniforms.", LILIN_PALACE_GATE_UNIFORMS);

			} else {
				return null;
			}
		}
	};
	
	public static final DialogueNodeOld LILIN_PALACE_GATE_UNIFORMS = new DialogueNodeOld("Lyssieth's Palace Gate", "", true, true) {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed(){
			return 5;
		}
		
		@Override
		public String getContent() {
			return UtilText.parseFromXMLFile("places/submission/submissionPlaces", "LILIN_PALACE_GATE_UNIFORMS");
		}


		@Override
		public Response getResponse(int responseTab, int index) {
			if (index == 2) {
				return new Response("Uniforms", "You are already asking Elizabeth about her uniform.", null);

			} else {
				return LILIN_PALACE_GATE.getResponse(responseTab, index);
			}
		}
	};
	
	public static final DialogueNodeOld LILIN_PALACE = new DialogueNodeOld("Lyssieth's Palace", "", false) {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed(){
			return 5;
		}
		
		@Override
		public String getContent() {
			return UtilText.parseFromXMLFile("places/submission/submissionPlaces", "LILIN_PALACE");
		}


		@Override
		public Response getResponse(int responseTab, int index) {
			return null;
		}
	};
	
	public static final DialogueNodeOld IMP_FORTRESS_1 = new DialogueNodeOld("Imp Fortress", "", false) {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed(){
			return 5;
		}
		
		@Override
		public String getContent() {
			return "<p>"
					+ "TODO: A large underground cave, and in the middle, a crude fortress has been built out of old wooden planks and pieces of sheet metal.</br>"
					+ "Each of the three imp fortresses will be a repeatable quest (clearing the fortress), which, once cleared, will pacify the surrounding tunnels for a week or so."
				+ "</p>"
				+ "<p>"
					+ "This particular fortress will be run by a single, very tough alpha-imp."
				+ "</p>";
		}


		@Override
		public Response getResponse(int responseTab, int index) {
			return null;
		}
	};
	
	public static final DialogueNodeOld IMP_FORTRESS_2 = new DialogueNodeOld("Imp Fortress", "", false) {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed(){
			return 5;
		}
		
		@Override
		public String getContent() {
			return "<p>"
					+ "TODO: A large underground cave, and in the middle, a crude fortress has been built out of old wooden planks and pieces of sheet metal.</br>"
					+ "Each of the three imp fortresses will be a repeatable quest (clearing the fortress), which, once cleared, will pacify the surrounding tunnels for a week or so."
				+ "</p>"
				+ "<p>"
					+ "This particular fortress will be made up of several groups of imps, who all share the same territory."
				+ "</p>";
		}


		@Override
		public Response getResponse(int responseTab, int index) {
			return null;
		}
	};
	
	public static final DialogueNodeOld IMP_FORTRESS_3 = new DialogueNodeOld("Imp Fortress", "", false) {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed(){
			return 5;
		}
		
		@Override
		public String getContent() {
			return "<p>"
					+ "TODO: A large underground cave, and in the middle, a crude fortress has been built out of old wooden planks and pieces of sheet metal.</br>"
					+ "Each of the three imp fortresses will be a repeatable quest (clearing the fortress), which, once cleared, will pacify the surrounding tunnels for a week or so."
				+ "</p>"
				+ "<p>"
					+ "This particular fortress will be a female-only imp clan, who prefer to use seduction rather than fighting physically."
				+ "</p>";
		}


		@Override
		public Response getResponse(int responseTab, int index) {
			return null;
		}
	};
	
	public static final DialogueNodeOld IMP_FORTRESS_4 = new DialogueNodeOld("Imp Fortress", "", false) {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed(){
			return 5;
		}
		
		@Override
		public String getContent() {
			return "<p>"
					+ "TODO: A large underground cave, and in the middle, a crude fortress has been built out of old wooden planks and pieces of sheet metal.</br>"
					+ "Each of the three imp fortresses will be a repeatable quest (clearing the fortress), which, once cleared, will pacify the surrounding tunnels for a week or so."
				+ "</p>"
				+ "<p>"
					+ "This particular fortress will be a male-only imp clan, who prefer to fight using dominant, physical attacks."
				+ "</p>";
		}


		@Override
		public Response getResponse(int responseTab, int index) {
			return null;
		}
	};
	
	public static final DialogueNodeOld IMP_FORTRESS_5 = new DialogueNodeOld("Imp Fortress", "", false) {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed(){
			return 5;
		}
		
		@Override
		public String getContent() {
			return "<p>"
					+ "TODO: A large underground cave, and in the middle, a crude fortress has been built out of old wooden planks and pieces of sheet metal.</br>"
					+ "Each of the three imp fortresses will be a repeatable quest (clearing the fortress), which, once cleared, will pacify the surrounding tunnels for a week or so."
				+ "</p>"
				+ "<p>"
					+ "This particular fortress will be run by a group of imp-slimes, who like mimicking imps."
				+ "</p>";
		}


		@Override
		public Response getResponse(int responseTab, int index) {
			return null;
		}
	};
	
	public static final DialogueNodeOld IMP_FORTRESS_6 = new DialogueNodeOld("Imp Fortress", "", false) {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed(){
			return 5;
		}
		
		@Override
		public String getContent() {
			return "<p>"
					+ "TODO: A large underground cave, and in the middle, a crude fortress has been built out of old wooden planks and pieces of sheet metal.</br>"
					+ "Each of the three imp fortresses will be a repeatable quest (clearing the fortress), which, once cleared, will pacify the surrounding tunnels for a week or so."
				+ "</p>"
				+ "<p>"
					+ "This particular fortress will be balanced between alpha-imp and regular imp encounters."
				+ "</p>";
		}


		@Override
		public Response getResponse(int responseTab, int index) {
			return null;
		}
	};

	// Entrance and exits:

	public static final DialogueNodeOld SEWER_ENTRANCE = new DialogueNodeOld("Enforcer Checkpoint", "", false) {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed(){
			return 5;
		}
		
		@Override
		public String getContent() {
			return "<p>"
						+ "A large stone building, spanning the entire width of the tunnel, has been erected here."
						+ " Signs reading 'Dominion Enforcer Post' are displayed above each of the entrances, and within, a dozen or so Enforcers are manning desks or standing guard beside internal doorways."
					+ "</p>"
					+ "<p>"
						+ "The Enforcers here don't seem to pay you much attention, allowing you to come and go as you please."
						+ " You notice that one of the desks looks like an information booth, and the Enforcer stationed behind it doesn't seem to be as busy as all the others."
						+ " If you wanted to, you could probably approach and ask for information about Submission..."
					+ "</p>";
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			if (index == 1) {
				return new ResponseEffectsOnly("Dominion", "Head back up to Dominion."){
					@Override
					public void effects() {
						Main.mainController.moveGameWorld(WorldType.DOMINION, PlaceType.DOMINION_EXIT_TO_SUBMISSION, true);
					}
				};

			} else if (index == 2) {
				return new Response("Information", "Ask about Submission society. <b>Not yet added!</b>", null);

			} else if (index == 3) {
				return new Response("Lyssieth", "Ask about Lyssieth. <b>Not yet added!</b>", null);

			} else {
				return null;
			}
		}
	};

}
