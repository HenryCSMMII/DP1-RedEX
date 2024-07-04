const updateAirportCapacities = () => {
  const newCapacities = { ...airportCapacities };

  data.flights.forEach((flight) => {
    flight.shipments.forEach((shipment) => {
      const currentDateTime = parseISO(`${tiempo_simulacion.dia_actual}T${tiempo_simulacion.tiempo_actual}`);
      const registerDateTime = parseISO(shipment.registerDateTime);
      const departureTime = shipment.departureTime ? parseISO(shipment.departureTime) : null;
      const arrivalTime = shipment.arrivalTime ? parseISO(shipment.arrivalTime) : null;

      // Registering shipment at departure airport
      if (currentDateTime >= registerDateTime && (!departureTime || currentDateTime < departureTime)) {
        const departureAirportCode = shipment.departureAirport.code;
        newCapacities[departureAirportCode].current += shipment.packageQuantity;
        const eventMessage = `Registrado ${shipment.packageQuantity} paquete(s) en el aeropuerto ${departureAirportCode} a las ${format(currentDateTime, 'HH:mm:ss')}. Capacidad actual: ${newCapacities[departureAirportCode].current}`;

        setConsoleEvents((prevEvents) => [...prevEvents, eventMessage]);

        if (newCapacities[departureAirportCode].current > newCapacities[departureAirportCode].max) {
          const exceso = newCapacities[departureAirportCode].current - newCapacities[departureAirportCode].max;
          const alertMessage = `Se excedió la capacidad máxima en el aeropuerto ${departureAirportCode}. 
                 Vuelo: ${flight.flight_number}, 
                 Hora: ${format(currentDateTime, 'HH:mm:ss')}, 
                 Capacidad actual: ${newCapacities[departureAirportCode].current}, 
                 Capacidad intentada: ${exceso + newCapacities[departureAirportCode].max}, 
                 Capacidad máxima: ${newCapacities[departureAirportCode].max}`;

          setConsoleEvents((prevEvents) => [...prevEvents, alertMessage]);
        }
      }

      // Removing shipment from departure airport at departure time
      if (departureTime && currentDateTime >= departureTime && currentDateTime < arrivalTime) {
        newCapacities[shipment.departureAirport.code].current -= shipment.packageQuantity;
        const eventMessage = `Salida de ${shipment.packageQuantity} paquete(s) del aeropuerto ${shipment.departureAirport.code} a las ${format(currentDateTime, 'HH:mm:ss')}. Capacidad actual: ${newCapacities[shipment.departureAirport.code].current}`;

        setConsoleEvents((prevEvents) => [...prevEvents, eventMessage]);
      }

      // Registering shipment at arrival airport at arrival time
      if (arrivalTime && currentDateTime >= arrivalTime) {
        const arrivalAirportCode = shipment.arrivalAirport.code;
        newCapacities[arrivalAirportCode].current += shipment.packageQuantity;
        const eventMessage = `Llegada de ${shipment.packageQuantity} paquete(s) al aeropuerto ${arrivalAirportCode} a las ${format(currentDateTime, 'HH:mm:ss')}. Capacidad actual: ${newCapacities[arrivalAirportCode].current}`;

        setConsoleEvents((prevEvents) => [...prevEvents, eventMessage]);

        if (newCapacities[arrivalAirportCode].current > newCapacities[arrivalAirportCode].max) {
          const exceso = newCapacities[arrivalAirportCode].current - newCapacities[arrivalAirportCode].max;
          const alertMessage = `Se excedió la capacidad máxima en el aeropuerto ${arrivalAirportCode}. 
                 Vuelo: ${flight.flight_number}, 
                 Hora: ${format(currentDateTime, 'HH:mm:ss')}, 
                 Capacidad actual: ${newCapacities[arrivalAirportCode].current}, 
                 Capacidad intentada: ${exceso + newCapacities[arrivalAirportCode].max}, 
                 Capacidad máxima: ${newCapacities[arrivalAirportCode].max}`;

          setConsoleEvents((prevEvents) => [...prevEvents, alertMessage]);
        }
      }

      // Remove shipment from accounting if it reached its final destination
      if (arrivalTime && currentDateTime >= arrivalTime && shipment.arrivalAirport.code === shipment.finalDestination.code) {
        newCapacities[shipment.arrivalAirport.code].current -= shipment.packageQuantity;
        const eventMessage = `Paquete(s) ${shipment.packageQuantity} llegó a su destino final ${shipment.arrivalAirport.code} a las ${format(currentDateTime, 'HH:mm:ss')}. Capacidad actual: ${newCapacities[shipment.arrivalAirport.code].current}`;

        setConsoleEvents((prevEvents) => [...prevEvents, eventMessage]);
      }
    });
  });

  setAirportCapacities(newCapacities);
};