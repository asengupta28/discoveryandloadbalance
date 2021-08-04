
/*	 ********************************************************	****
****															****
**** This application demonstrates client-side Load-Balancing 	****
**** using org.springframework.web.client.RestTemplate and	  	****
**** org.springframework.cloud.client.discovery.DiscoveryClient	**** 
**** to dynamically use APIs from different instances of		****
**** "spring-cloud-eureka-client" running under different ports	****
**** transparent to itself.										****
****															****
**** ********************************************************	***/

package com.msa.discoveryandloadbalance;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class DiscoveryAndLoadbalance
{
	public static void main(String[] args) {SpringApplication.run(DiscoveryAndLoadbalance.class, args); }
}

@RestController
class ServiceInstanceRestController
{
	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
    RestTemplate restTemplate;

	// This method takes a Parameter - the value of the Parameter = Name in which an Application is registered in the Eureka Server
	// Look in Eureka Server's console and in the frame with tile "Instances currently registered with Eureka"
	// Pick any values from column title "Application" displayed below

	// The fixed path "spring-cloud-eureka-client" used in the URI is the name of the Application
	// that's called transparently [w/o knowing Port number] by the help of "LoadBalanced"
	@RequestMapping("/service-instances/2/{applicationName}")
	public String getStudents(@PathVariable String applicationName) 
	{
		String response = restTemplate.exchange("http://spring-cloud-eureka-client/service-instances/{applicationName}",
                                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}, applicationName).getBody();

		System.out.println("Response Received as " + response);

		return (response);
	}
 
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate()
	{
        return new RestTemplate();
    }
}