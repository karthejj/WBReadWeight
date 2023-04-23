package com.example.WBReadWeight;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.example.WBReadWeight.entity.InitialWeight;
import com.example.WBReadWeight.repository.InitialWeightRepository;
import com.fazecast.jSerialComm.SerialPort;

@Component
public class MyApplicationRunner implements ApplicationRunner {

	@Autowired
	private InitialWeightRepository rawWeightRepository;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		// TODO Auto-generated method stub
		
		try {
			SerialPort comPort = SerialPort.getCommPorts()[2];
			System.out.println(comPort.getDescriptivePortName());
			System.out.println(comPort.getPortDescription());
			System.out.println(comPort.getSystemPortName());
//			System.out.println(comPort.get);
			comPort.openPort();
			comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
//			InputStream in = comPort.getInputStream();
			ByteArrayInputStream in = new ByteArrayInputStream("US,NT,-113.0254kg\nUS,NT,-113.0254kg\nUS,NT,-113.0254kg\n".getBytes());
			try
			{
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String line;
				while ((line = reader.readLine()) != null) {
//					System.out.println(line);
				    String[] numbers = line.split("[^\\d\\.]+");
				    if(!numbers[1].equals("0") && !numbers[1].equals("0.0")) {
				    	System.out.println(numbers[1]);
//				    	rwDTO.setWeight(numbers[1]);
				    	 InitialWeight rw = new InitialWeight();
				     	rw.setCreatedDate(LocalDateTime.now());
				     	rw.setVehicleWeight(numbers[1]);
				     	InitialWeight rwRecord = rawWeightRepository.save(rw);
				             System.out.println(rwRecord.getRawWeightId());
				             System.out.println(rwRecord.getVehicleWeight());
				             System.out.println(rwRecord.getCreatedDate());
				    	break;
				    }
				}
//             InitialWeight rw = new InitialWeight();
//     		rw.setCreatedDate(LocalDateTime.now());
//     		rw.setVehicleWeight(rwDTO.getWeight());
//     		InitialWeight rwRecord = rawWeightRepository.save(rw);
//     		rwDTO.setId(rwRecord.getRawWeightId());
//     		rwDTO.setCreatedDate(rwRecord.getCreatedDate().toString());
             
//             System.out.println(rwDTO.getId());
//             System.out.println(rwDTO.getWeight());
//             System.out.println(rwDTO.getCreatedDate());
             in.close();
			} catch (Exception e) { e.printStackTrace(); }
			comPort.closePort();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
