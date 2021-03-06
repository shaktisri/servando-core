/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.usc.citius.servando.android.communications.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

import es.usc.citius.servando.android.xml.converters.numberListConverters.ShortListConverter;

/**
 * Esta clase sirve como clase de utilidad para enviar y recibir grandes listas de números enteros de 16 bits.
 * 
 * @author Tomás Teijeiro Campo
 */
@Root(name = "shortList", strict = false)
@Convert(value = ShortListConverter.class)
public class ShortList {

	/**
	 * Conjunto de cadenas de caracteres para transportar metadatos.
	 */
	private ArrayList<String> metaData;
	/**
	 * Conjunto de valores de tipo short que transporta este objeto
	 */
	private short[] values;

	public ShortList()
	{
		this(0);
	}

	/**
	 * Crea una nueva lista de enteros de 16 bits del tamaño especificado.
	 * 
	 * @param size
	 */
	public ShortList(int size)
	{
		metaData = new ArrayList<String>();
		values = new short[size];
	}

	/**
	 * Crea una lista de enteros a partir de su representación en binario, con 2 bytes en formato little endian por cada
	 * número.
	 * 
	 * @param binaryList
	 */
	public ShortList(byte[] binaryList)
	{
		this(binaryList, ByteOrder.LITTLE_ENDIAN);
	}

	/**
	 * Crea una lista de números en enteros a partir de su representación en binario, con 2 bytes por cada número en el
	 * formato indicado.
	 * 
	 * @param binaryList
	 * @param byteOrder Formato utilizado en la codificación binaria (little-endian o big-endian)
	 */
	public ShortList(byte[] binaryList, ByteOrder byteOrder)
	{
		metaData = new ArrayList<String>();
		values = new short[binaryList.length / 2];
		ByteBuffer b = ByteBuffer.wrap(binaryList);
		b.order(byteOrder);
		b.asShortBuffer().get(values);
	}

	/**
	 * Muestra esta lista de números enteros en una representación binaria en formato little-endian. Los metadatos se
	 * ignorarán.
	 * 
	 * @return Array de bytes con la lista de números en formato little endian.
	 */
	public byte[] toByteArray()
	{
		return toByteArray(ByteOrder.LITTLE_ENDIAN);
	}

	/**
	 * Muestra esta lista de números enteros en una representación binaria en el formato elegido. Los metadatos se
	 * ignorarán.
	 * 
	 * @param order Orden de los bytes (little-endian o big-endian)
	 * @return Array de bytes con la lista de números en el formato elegido.
	 */
	public byte[] toByteArray(ByteOrder order)
	{
		byte[] buf = new byte[2 * values.length];
		int off = 0;
		if (order == ByteOrder.LITTLE_ENDIAN)
		{
			for (short v : values)
			{
				buf[off++] = (byte) v;
				buf[off++] = (byte) (v >> 8);
			}
		} else
		{
			for (short v : values)
			{
				buf[off++] = (byte) (v >> 8);
				buf[off++] = (byte) v;
			}
		}
		return buf;
	}

	/**
	 * Conjunto de cadenas de caracteres para transportar metadatos.
	 * 
	 * @return the metaData
	 */
	public ArrayList<String> getMetaData()
	{
		return metaData;
	}

	/**
	 * Conjunto de cadenas de caracteres para transportar.
	 * 
	 * @param metaData the metaData to set
	 */
	public void setMetaData(ArrayList<String> metaData)
	{
		this.metaData = metaData;
	}

	/**
	 * Conjunto de valores de tipo short que transporta este objeto
	 * 
	 * @return the values
	 */
	public short[] getValues()
	{
		return values;
	}

	/**
	 * Conjunto de valores de tipo short que transporta este objeto
	 * 
	 * @param values the values to set
	 */
	public void setValues(short[] values)
	{
		this.values = values;
	}
}
