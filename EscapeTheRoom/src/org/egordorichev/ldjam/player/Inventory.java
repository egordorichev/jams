package org.egordorichev.ldjam.player;

import org.egordorichev.ldjam.Item;

public class Inventory {
	private int size;
	private Item[] items;

	public Inventory(int size) {
		this.size = size;
		this.items = new Item[size];
	}

	public void addItem(Item item) {
		for(int i = 0; i < this.size; i++) {
			if(this.items[i] == null) {
				this.items[i] = item;

				return;
			}
		}

		throw new RuntimeException("Inventory is full");
	}

	public void removeItem(Item item) {
		for(int i = 0; i < this.size; i++) {
			if(this.items[i] == item) {
				this.items[i] = null;

				return;
			}
		}
	}

	public Item getItem(int index) {
		if(index > this.size || index < 0) {
			throw new RuntimeException("Trying to get item with invalid index from inventory");
		}

		return this.items[index];
	}

	public boolean hasItem(Item item) {
		for(int i = 0; i < this.size; i++) {
			if(this.items[i] == item) {
				return true;
			}
		}

		return false;
	}

	public boolean isFull() {
		for(int i = 0; i < this.size; i++) {
			if(this.items[i] == null) {
				return false;
			}
		}

		return true;
	}

	public int getSize() {
		return this.size;
	}
}