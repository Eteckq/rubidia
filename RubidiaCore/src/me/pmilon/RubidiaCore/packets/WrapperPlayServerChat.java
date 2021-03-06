package me.pmilon.RubidiaCore.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers.ChatType;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

public class WrapperPlayServerChat extends AbstractPacket {
	
	public static final PacketType TYPE = PacketType.Play.Server.CHAT;

	public WrapperPlayServerChat() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerChat(PacketContainer packet) {
		super(packet, TYPE);
	}

	/**
	 * Retrieve the chat message.
	 * <p>
	 * Limited to 32767 bytes
	 * 
	 * @return The current message
	 */
	public WrappedChatComponent getMessage() {
		return handle.getChatComponents().read(0);
	}

	/**
	 * @deprecated Renamed to {@link #getMessage()}
	 */
	@Deprecated
	public WrappedChatComponent getJsonData() {
		return getMessage();
	}

	/**
	 * Set the message.
	 * 
	 * @param value - new value.
	 */
	public void setMessage(WrappedChatComponent value) {
		handle.getChatComponents().write(0, value);
	}

	/**
	 * @deprecated Renamed to {@link #setMessage(WrappedChatComponent)}
	 */
	@Deprecated
	public void setJsonData(WrappedChatComponent value) {
		setMessage(value);
	}

	/**
	 * Retrieve Position.
	 * <p>
	 * Notes: 0 - Chat (chat box) ,1 - System Message (chat box), 2 - Above
	 * action bar
	 * 
	 * @return The current Position
	 */
	public ChatType getPosition() {
		return handle.getChatTypes().read(0);
	}

	/**
	 * Set Position.
	 * 
	 * @param value - new value.
	 */
	public void setPosition(ChatType value) {
		handle.getChatTypes().write(0, value);
	}

}