package bptree.simple;

import bptree.LeafNode;
import bptree.Node;
import bptree.BPlusTree.InvalidDeletionException;

/**
 * The {@code SimpleLeafNode} class is a simple implementation of the {@code LeafNode} interface.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 * 
 * @param <K>
 *            the type of keys
 * @param <V>
 *            the type of values
 */
public class SimpleLeafNode<K extends Comparable<K>, V> extends SimpleNode<K>
		implements LeafNode<K, V> {

	/**
	 * An automatically generated serial version UID.
	 */
	private static final long serialVersionUID = 2590729339527002169L;

	/**
	 * Constructs a {@code SimpleLeafNode}.
	 * 
	 * @param degree
	 *            the degree of the {@code SimpleLeafNode}
	 */
	public SimpleLeafNode(int degree) {
		super(degree);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(K k) throws InvalidDeletionException {
		int i = 0;
		for (; i < keyCount; i++) {
			if (keys[i].compareTo(k) == 0) {
				for (int j = i; j < keyCount - 1; j++) {
					keys[j] = keys[j + 1];
					pointers[j] = pointers[j + 1];
				}
				break;
			}
		}
		if (i == keyCount)
			throw new InvalidDeletionException("key: " + k);
		keyCount--;
		keys[keyCount] = null;
		pointers[keyCount] = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isUnderUtilized() {
		// Check if this node has fewer keys than the minimum required
		// Min keys = ceil(max_keys / 2), where max_keys = degree - 1
		int maxKeys = keys.length;
		int minKeys = (int) Math.ceil(maxKeys / 2.0);
		return keyCount < minKeys;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean mergeable(Node<K> other) {
		// Check if both nodes' entries can fit in a single node
		int maxKeys = keys.length;
		return keyCount + other.keyCount() <= maxKeys;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insert(K k, V v) {
		if (keyCount == 0 || k.compareTo(keys[0]) < 0)
			insert(0, k, v);
		else {
			int i = findIndexL(k);
			insert(i + 1, k, v);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insert(int i, K k, V v) {
		for (int j = keyCount; j > i; j--) {
			keys[j] = keys[j - 1];
			pointers[j] = pointers[j - 1];
		}
		keys[i] = k;
		pointers[i] = v;
		keyCount++;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAt(int i) {
		for (int j = i; j < keyCount - 1; j++) {
			keys[j] = keys[j + 1];
			pointers[j] = pointers[j + 1];
		}
		keys[keyCount - 1] = null;
		pointers[keyCount - 1] = null;
		keyCount--;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(K k) {
		for (int i = 0; i < keyCount; i++)
			if (keys[i].compareTo(k) == 0)
				return true;
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSuccessor(LeafNode<K, V> n) {
		pointers[pointers.length - 1] = n;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public LeafNode<K, V> successor() {
		return (LeafNode<K, V>) pointers[pointers.length - 1];
	}

	/**
	 * Returns the largest index i such that keys[i] is smaller than the given key.
	 * 
	 * @param key
	 *            a key
	 * @return the largest index i such that keys[i] is smaller than the given key; -1 if there is no such i
	 */
	protected int findIndexL(K key) {
		for (int i = keyCount - 1; i >= 0; i--) {
			if (keys[i].compareTo(key) < 0)
				return i;
		}
		return -1;
	}

}
