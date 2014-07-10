package com.octopod.mgframe.event;

public abstract class Event extends SynchronizableEvent {

	private boolean post = false;

	/**
	 * Returns if this is run before or after the event.
	 * @return
	 */
	public boolean isPostProcess() {return post;}

	/**
	 * Don't use this inside an event listener!
	 * Lets the event manager set the 'post' variable
	 * @param post
	 */
	public void setPostProcess(boolean post) {this.post = post;}

}
