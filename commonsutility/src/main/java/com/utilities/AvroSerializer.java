/*
 * @author
 *  Nidhi Chourasia created on 2018.
 */

package com.utilities;

/*Schema for Avro serialization is defined in /main/avro.
 * Compiled and Employee class is generated*/

import com.employee.Employee;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static java.lang.String.valueOf;

public class AvroSerializer {

    Schema schema = Employee.getClassSchema();

    public AvroSerializer() throws IOException {
    }

    //Avro Serialization
    public <T> byte[] serialize(T object) throws IOException {
        DatumWriter<T> avroWriter = new SpecificDatumWriter<T>(getSchema(object));
        DataFileWriter<T> dataFileWriter = new DataFileWriter<T>(avroWriter);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        EncoderFactory encoderFactory = new EncoderFactory();
        BinaryEncoder binaryEncoder = encoderFactory.binaryEncoder(byteArrayOutputStream, null);

        avroWriter.write(object, binaryEncoder);
        binaryEncoder.flush();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    //Avro Deserialization
    public <T> String deSerialize(byte[] data) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        DecoderFactory decoderFactory = new DecoderFactory();
        DatumReader datumReader = new GenericDatumReader<GenericRecord>(schema);

        BinaryDecoder binaryDecoder = decoderFactory.binaryDecoder(byteArrayInputStream, null);

        try {
            byteArrayInputStream.close();
            return valueOf(datumReader.read(null, binaryDecoder));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    //Deserializatio by providing schema file path
    public <T> T deserialize(byte[] data, byte[] schemaFile) {
        if (schemaFile == null) {
            throw new IllegalArgumentException("Schema File cannot be null..");
        }
        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse(new String(schemaFile));

        return deSerialize(data, schema);
    }

    public <T> T deSerialize(byte[] data, Schema schema) {
        DatumReader<T> datumReader = new SpecificDatumReader<>(schema);
        return deSerialize(data, datumReader);
    }

    public <T> T deSerialize(byte[] data, DatumReader<T> datumReader) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        DecoderFactory decoderFactory = new DecoderFactory();

        BinaryDecoder binaryDecoder = decoderFactory.binaryDecoder(byteArrayInputStream, null);

        try {
            byteArrayInputStream.close();
            return datumReader.read(null, binaryDecoder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T deserialize(byte[] data, byte[] actualSchema, byte[] expectedSchema) {
        Schema originalSchema = new Schema.Parser().parse(new String(actualSchema));
        Schema newSchema = new Schema.Parser().parse(new String(expectedSchema));

        DatumReader<T> tDatumReader = new SpecificDatumReader<>(originalSchema, newSchema);
        return deSerialize(data, tDatumReader);
    }

    private <T> Schema getSchema(T object) {

        Method getClassSchema = null;
        try {
            getClassSchema = object.getClass().getDeclaredMethod("getClassSchema");
            return (Schema) getClassSchema.invoke(object);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        AvroSerializer avroSerializer = new AvroSerializer();

        Employee employee = Employee.newBuilder()
                .setName("Nidhi")
                .setAddress("US")
                .setAge(30)
                .setSalary(50000)
                .setId(111)
                .build();

        byte[] serialize = avroSerializer.serialize(employee);
        System.out.println(Arrays.toString(serialize));

        String deserializedData = avroSerializer.deSerialize(serialize);
        System.out.println(deserializedData);
    }

}
