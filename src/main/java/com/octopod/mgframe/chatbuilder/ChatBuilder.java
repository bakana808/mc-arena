package com.octopod.mgframe.chatbuilder;

import com.octopod.mgframe.chatbuilder.ChatUtils.*;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONValue;

import java.util.*;

/**
 * Last Updated: 2.15.2014
 * ChatBuilder to build messages for Minecraft's new JSON chat.
 * Utitlizes "method chaining."
 * @author Octopod
 */
public class ChatBuilder {

    private ArrayList<ChatElement> elements = new ArrayList<>();

    private int elementMarker = -1;
    private ChatElement element = null;

    public ChatBuilder() {}

    public ChatBuilder(String text) {
        append(text);
    }

    private boolean indexInRange(int i) {
        return (i >= 0 && i < elements.size());
    }

    /**
     * The total amount of elements.
     * @return size of elements.
     */
    public int size() {return elements.size();}

    /**
     * Returns the current elements. (A list of elements)
     * @return the current elements
     */
    public ArrayList<ChatElement> toElementList() {
        return elements;
    }

    public ChatBuilder insert(int index, List<ChatElement> elements) {
        if(indexInRange(index)) {
            this.elements.addAll(elements);
        }
        return selectLast();
    }

    /**
     * Inserts every element in the provided builder to the current list of elements.
     * @return
     */
    public ChatBuilder insert(int index, ChatBuilder builder) {
        return insert(index, new ArrayList<>(builder.toElementList()));
    }

	/**
	 * Gets the last ChatElement
	 * @return The last ChatElement.
	 */
	public ChatElement getLastElement() {
		return elements.get(elements.size() - 1);
	}

	/**
	 * Gets the currently selected ChatElement.
	 * @return The currently selected ChatElement.
	 */
	public ChatElement getCurrentElement() {
		return element;
	}

    public int getCurrentElementIndex() {
        return elementMarker;
    }

	/**
	 * Gets the ChatElement at the specified index. Returns null if out of bounds.
	 * @return The ChatElement from the index, or null if not found.
	 */
	public ChatElement getElementAt(int i) {
		try {
			return elements.get(i);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * Manually selects the current ChatElement.
	 * @param index The index to select.
	 */
	public ChatBuilder selectElement(int index) {
        if(indexInRange(index)) {
            element = elements.get(index);
            elementMarker = index;
        }
		return this;
	}

    /**
     * Selects the last ChatElement.
     */
    public ChatBuilder selectLast() {
        return selectElement(size() - 1);
    }

	/**
	 * Pushes a new message to the end of the ChatBuilder, as a new ChatElement.
	 * It will also select the last element.
	 * @param message The message to push.
	 */
	public ChatBuilder append(String message) {
		elements.add(new ChatElement(message));
		return selectLast();
	}

    public ChatBuilder appendLegacy(String message) {
        return append(ChatUtils.colorize(message));
    }

    public ChatBuilder appendLegacy(String message, char code) {
        return append(ChatUtils.colorize(message, code));
    }

	/**
	 * Pushes a new ChatElement to the end of the ChatBuilder.
	 * It will also select the last element.
	 * @param elements the list of ChatElements to push.
	 */
    public ChatBuilder append(List<ChatElement> elements) {
        this.elements.addAll(elements);
        return selectLast();
    }

    public ChatBuilder append(Object object) {
        return append(object.toString());
    }

	public ChatBuilder append(ChatElement... elements) {
        return append(Arrays.asList(elements));
	}

    public ChatBuilder append(ChatBuilder builder) {
        return append(builder.toElementList());
    }

    public ChatBuilder appendFront(List<ChatElement> elements) {
        this.elements.addAll(0, elements);
        return selectElement(elements.size() - 1);
    }

    public ChatBuilder appendFront(ChatElement... elements) {
        return append(Arrays.asList(elements));
    }

    /**
     * Appends a space to the end of the ChatBuilder.
     * @return the ChatBuilder
     */
    public ChatBuilder sp() {
        return append(' ');
    }

    /**
     * Appends any amount of spaces to the end of the ChatBuilder.
     * @param x the amount of spaces
     * @return the ChatBuilder
     */
    public ChatBuilder sp(int x) {
        char[] spaces = new char[x];
        Arrays.fill(spaces, ' ');
        return append(new String(spaces));
    }

	/**
	 * Pushes filler to the end of the ChatBuilder, as a new ChatElement.
	 * Fillers fit text to a pixel width (according to Minecraft's default font)
	 * Fillers will contain filler characters if the width is too abnormal.
	 * If you want to avoid filler characters, make sure the width is divisible by 4. (the width of a space)
	 * Unexpected behavior might occur if used with the translate feature of MC's new chat system.
	 * It will also select the last element.
	 * @param width The width of the filler.
	 */
	public ChatBuilder appendFiller(int width) {
		elements.add(ChatUtils.fillerElement(width));
		return selectLast();
	}

	/**
	 * Returns the width of the current element in pixels, according to Minecraft's default font.
	 * @return the width of the current element, in pixels.
	 */
	public int getWidth() {
		return ChatUtils.width(element.getText());
	}

	/**
	 * Sets the click event of the currently selected ChatElement.
	 * @param event The ChatHoverEvent to use.
	 * @param value The value, as a string.
	 */
	public ChatBuilder click(ClickEvent event, String value) {
		if(element != null)
			element.setClickEvent(event, value);
		return this;
	}

		public ChatBuilder run(String command) {
			return click(ClickEvent.RUN_COMMAND, command);
		}

		public ChatBuilder suggest(String command) {
			return click(ClickEvent.SUGGEST_COMMAND, command);
		}

		public ChatBuilder link(String url) {
			return click(ClickEvent.OPEN_URL, url);
		}

		public ChatBuilder file(String path) {
			return click(ClickEvent.OPEN_FILE, path);
		}

	/**
	 * Sets the setHoverEvent event of the currently selected ChatElement.
	 * @param event The ChatHoverEvent to use.
	 * @param value The value, as a string.
	 */
	public ChatBuilder hover(HoverEvent event, String value) {
		if(element != null)
			element.setHoverEvent(event, value);
		return this;
	}

		public ChatBuilder tooltip(String... lines) {
			return hover(HoverEvent.SHOW_TEXT, StringUtils.join(lines, "\n"));
		}

		public ChatBuilder tooltip(ChatBuilder builder) {
			return hover(HoverEvent.SHOW_TEXT, builder.toLegacy());
		}

		public ChatBuilder achievement(String name) {
			return hover(HoverEvent.SHOW_ACHIEVEMENT, name);
		}

		public ChatBuilder item(String json) {
			return hover(HoverEvent.SHOW_ITEM, json);
		}

	/**
	 * Change the color of the currently selected ChatElement. Non-color ChatColors will be ignored.
	 * @param color The new color of the current element.
	 */
	public ChatBuilder color(ChatColor color) {
		if(element != null)
			element.setColor(color);
		return this;
	}

	/**
	 * Apply formats to the currently selected ChatElement. Non-format ChatColors will not apply.
	 * @param formats The formats to apply to the current element.
	 */
	public ChatBuilder format(ChatFormat... formats) {
		if(element != null)
			element.format(formats);
		return this;
	}

	//Shortcuts for format()

	public ChatBuilder bold() 			{return format(ChatFormat.BOLD);}
	public ChatBuilder italic() 		{return format(ChatFormat.ITALIC);}
	public ChatBuilder underline() 		{return format(ChatFormat.UNDERLINED);}
	public ChatBuilder strikethrough() 	{return format(ChatFormat.STRIKETHROUGH);}
	public ChatBuilder obfuscate() 		{return format(ChatFormat.OBFUSCATED);}

	/**
	 * Sends the player this object represented as a chat message.
	 * @param player The player that the message will be sent to.
	 */
	public void send(AbstractPlayer player) {
		ChatUtils.send(player, this);
	}
	
	/**
	 * Returns this object as a appendLegacy chat string. Actually just a shortcut to the static toLegacy method.
	 * @return Legacy chat string
	 */
	public String toLegacy() {
		return ChatUtils.toLegacy(this);
	}

    public String toString()
    {
        Map<String, Object> json = new HashMap<>();

        json.put("text", "");
        json.put("extra", toElementList());

        return JSONValue.toJSONString(json);
    }

}
