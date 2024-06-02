import React from 'react';
import styled from 'styled-components';

const LegendWrapper = styled.div`
  padding: 10px 20px;
  box-sizing: border-box;
  width: 100%;
`;

const LegendContainer = styled.div`
  background-color: #000;
  color: #fff;
  padding: 20px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: flex-start;
  flex-wrap: wrap;

  @media (min-width: 768px) {
    flex-wrap: nowrap;
  }
`;

const LegendTitle = styled.div`
  text-align: left;
  margin-bottom: 10px;
  font-weight: bold;
  font-size: 1.2em;
`;

const LegendItemsContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  width: 100%;

  @media (min-width: 768px) {
    flex-wrap: nowrap;
  }
`;

const LegendItem = styled.div`
  display: flex;
  align-items: center;
  margin: 5px 10px;

  span {
    margin-left: 10px;
  }
`;

const colorBox = (color) => styled.div`
  width: 20px;
  height: 20px;
  background-color: ${color};
  border-radius: 3px;
`;

const Legend = () => {
  return (
    <LegendWrapper>
      <LegendContainer>
        <LegendTitle>Leyenda</LegendTitle>
        <LegendItemsContainer>
          <LegendItem>
            <Box color="#00FF00" />
            <span>No saturado</span>
          </LegendItem>
          <LegendItem>
            <Box color="#FFA500" />
            <span>Semi-saturado</span>
          </LegendItem>
          <LegendItem>
            <Box color="#FF0000" />
            <span>Saturado</span>
          </LegendItem>
          <LegendItem>
            <span>✈️ Vuelos</span>
          </LegendItem>
          <LegendItem>
            <span>📦 Almacén en aeropuerto</span>
          </LegendItem>
        </LegendItemsContainer>
      </LegendContainer>
    </LegendWrapper>
  );
};

const Box = ({ color }) => {
  const ColorBox = colorBox(color);
  return <ColorBox />;
};

export default Legend;