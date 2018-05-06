package me.pmilon.RubidiaQuests.ui;

import java.util.Arrays;

import me.pmilon.RubidiaCore.Core;
import me.pmilon.RubidiaCore.RManager.RPlayer;
import me.pmilon.RubidiaCore.ui.ConfirmationUI;
import me.pmilon.RubidiaQuests.pnjs.QuestPNJ;
import me.pmilon.RubidiaQuests.quests.Quest;

public class QuestDeletionConfirmationUI extends ConfirmationUI {

	private Quest quest;
	private QuestPNJ pnj;
	public QuestDeletionConfirmationUI(RPlayer rp, Quest quest, QuestPNJ pnj) {
		super(rp.getPlayer(), rp.translateString("Quest deletion", "Suppression de qu�te"), new String[]{rp.translateString("�aDelete quest", "�aSupprimer la qu�te"), rp.translateString("�cKeep quest", "�cConserver la qu�te")},
				quest.getColoredTitle(), Arrays.asList(quest.getColoredSubtitle(), rp.translateString("�8Are you sure you want to delete this quest?", "�8�tes-vous s�r de vouloir supprimer cette qu�te ?")));
		this.quest = quest;
		this.pnj = pnj;
	}
	
	@Override
	protected void no() {
	}
	
	@Override
	protected void yes() {
		this.getQuest().delete();
		Core.uiManager.requestUI(new PNJQuests(this.getHolder(), this.getPnj()));
	}

	public Quest getQuest() {
		return quest;
	}

	public void setQuest(Quest quest) {
		this.quest = quest;
	}

	public QuestPNJ getPnj() {
		return pnj;
	}

	public void setPnj(QuestPNJ pnj) {
		this.pnj = pnj;
	}

}
