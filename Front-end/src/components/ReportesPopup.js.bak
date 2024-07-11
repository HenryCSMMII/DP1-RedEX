import React, { useEffect, useState } from 'react';
import Modal from 'react-modal';
import styled from 'styled-components';
import { Bar } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend } from 'chart.js';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

const ModalContent = styled.div`
  background: #fff;
  padding: 20px;
  max-width: 1200px; /* Ajusta el ancho según sea necesario */
  width: 90%;
  margin: 40px auto;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  z-index: 9999999999;
`;

const FiltersContainer = styled.div`
  margin-bottom: 20px;
  z-index: 9999999999;
`;

const FiltersTitle = styled.h3`
  margin: 0 0 10px 0;
  display: flex;
  align-items: center;
  z-index: 9999999999;
`;

const FilterIcon = styled.span`
  margin-left: 10px;
  transform: rotate(90deg);
  display: inline-block;
`;

const FilterLabel = styled.label`
  margin: 0 10px 10px 0;
  display: flex;
  flex-direction: column;
  font-weight: bold;
`;

const FilterInput = styled.input`
  padding: 5px;
  margin-top: 5px;
  border: 1px solid #ccc;
  border-radius: 4px;
  width: 200px; /* Ajustar el ancho */
`;

const FiltersRow = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: space-between;
`;

const HeaderContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
`;

const ChartContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
  width: 100%;
`;

const ReportesPopup = ({ isOpen, onRequestClose, airportCapacities }) => {
  const [chartData, setChartData] = useState(null);

  useEffect(() => {
    if (airportCapacities) {
      const sortedCapacities = Object.entries(airportCapacities)
        .map(([code, { current_capacity, max_capacity }]) => ({
          code,
          saturation: ((current_capacity / max_capacity) * 100).toFixed(2),
        }))
        .sort((a, b) => b.saturation - a.saturation)
        .slice(0, 5);

      const labels = sortedCapacities.map((airport) => airport.code);
      const data = sortedCapacities.map((airport) => parseFloat(airport.saturation));

      setChartData({
        labels,
        datasets: [
          {
            label: 'Saturación (%)',
            data,
            backgroundColor: 'rgba(255, 99, 132, 0.2)',
            borderColor: 'rgba(255, 99, 132, 1)',
            borderWidth: 1,
          },
        ],
      });
    }
  }, [airportCapacities]);

  return (
    <Modal
      isOpen={isOpen}
      onRequestClose={onRequestClose}
      style={{
        overlay: {
          backgroundColor: 'rgba(0, 0, 0, 0.75)',
          zIndex: 9999999998,
        },
        content: {
          top: '50%',
          left: '50%',
          right: 'auto',
          bottom: 'auto',
          marginRight: '-50%',
          transform: 'translate(-50%, -50%)',
          padding: 0,
          border: 'none',
          background: 'none',
          width: '80%',
          height: '80%',
          zIndex: 9999999999,
        },
      }}
    >
      <ModalContent>
        <HeaderContainer>
          <h2>Reportes</h2>
        </HeaderContainer>

        <ChartContainer>
          {chartData && (
            <Bar
              data={chartData}
              options={{
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                  legend: {
                    position: 'top',
                  },
                  title: {
                    display: true,
                    text: 'Saturación de Aeropuertos (%)',
                  },
                },
              }}
            />
          )}
        </ChartContainer>
      </ModalContent>
    </Modal>
  );
};

export default ReportesPopup;