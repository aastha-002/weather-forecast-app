import { useState } from "react";
import "./App.css";

function getWeatherIcon(iconCode) {

  if (!iconCode) {
    return "🌤️";
  }

  if (iconCode.startsWith("01")) {
    return "☀️";
  }

  if (iconCode.startsWith("02")) {
    return "🌤️";
  }

  if (iconCode.startsWith("03")) {
    return "☁️";
  }

  if (iconCode.startsWith("04")) {
    return "☁️";
  }

  if (iconCode.startsWith("09")) {
    return "🌧️";
  }

  if (iconCode.startsWith("10")) {
    return "🌦️";
  }

  if (iconCode.startsWith("11")) {
    return "⛈️";
  }

  if (iconCode.startsWith("13")) {
    return "❄️";
  }

  if (iconCode.startsWith("50")) {
    return "🌫️";
  }

  return "🌤️";
}


function App() {

  const [city, setCity] = useState("");
  const [weather, setWeather] = useState(null);
  const [forecast, setForecast] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);


  const searchWeather = async () => {

    if (!city.trim()) {
      setError("Please enter a city name");
      return;
    }

    setLoading(true);
    setError("");


    try {

      const weatherResponse = await fetch(
        `http://localhost:8080/api/weather/current?city=${encodeURIComponent(city)}`
      );


      if (!weatherResponse.ok) {
        throw new Error("City not found");
      }


      const weatherData =
        await weatherResponse.json();


      const forecastResponse = await fetch(
        `http://localhost:8080/api/weather/forecast?city=${encodeURIComponent(city)}`
      );


      if (!forecastResponse.ok) {
        throw new Error("Forecast not available");
      }


      const forecastData =
        await forecastResponse.json();


      setWeather(weatherData);


      const forecastList =
        forecastData.forecastItems ||
        forecastData.forecast ||
        forecastData.items ||
        [];


      setForecast(
        forecastList.slice(0, 5)
      );


    } catch (err) {

      setWeather(null);
      setForecast([]);

      setError(
        "City not found. Please try another city."
      );
    }


    setLoading(false);
  };


  const formatDate = (dateString, index) => {

    if (index === 0) {
      return "Today";
    }


    const date =
      new Date(dateString + "T12:00:00");


    return date.toLocaleDateString(
      "en-IN",
      {
        weekday: "short",
        day: "numeric",
        month: "short"
      }
    );
  };


  return (

    <div className="app">

      <div className="weather-card">

        <h1>
          Weather Forecast
        </h1>


        <p className="subtitle">
          Check the current weather and 5-day forecast
        </p>


        <div className="search-box">

          <input
            type="text"
            placeholder="Enter city name"
            value={city}
            onChange={(e) =>
              setCity(e.target.value)
            }
            onKeyDown={(e) => {

              if (e.key === "Enter") {
                searchWeather();
              }

            }}
          />


          <button onClick={searchWeather}>
            Search
          </button>

        </div>


        {loading && (

          <p className="loading">
            Loading weather...
          </p>

        )}


        {error && (

          <p className="error">
            {error}
          </p>

        )}


        {weather && !loading && (

          <>

            {/* CURRENT WEATHER */}

            <div className="current-weather">

              <h2>
                {weather.city}
              </h2>


              <div className="temperature">
                {weather.temperature.toFixed(1)}°C
              </div>


              <p className="description">
                {weather.description}
              </p>


              <p className="humidity">
                💧 Humidity: {weather.humidity}%
              </p>

            </div>


            {/* 5-DAY FORECAST */}

            <div className="forecast-section">

              <h2>
                5-Day Forecast
              </h2>


              <div className="forecast-container">

                {forecast.map(
                  (item, index) => (

                    <div
                      className="forecast-card"
                      key={item.dateTime}
                    >

                      <h3>
                        {formatDate(
                          item.dateTime,
                          index
                        )}
                      </h3>


                      <div className="weather-icon">

                        {getWeatherIcon(
                          item.icon
                        )}

                      </div>


                      <div className="temperature-range">

                        <div className="min-temperature">

                          <span>
                            Min
                          </span>

                          <strong>
                            {item.minTemperature.toFixed(1)}°C
                          </strong>

                        </div>


                        <div className="max-temperature">

                          <span>
                            Max
                          </span>

                          <strong>
                            {item.maxTemperature.toFixed(1)}°C
                          </strong>

                        </div>

                      </div>


                      <p className="forecast-description">
                        {item.description}
                      </p>

                    </div>

                  )
                )}

              </div>

            </div>

          </>

        )}

      </div>

    </div>
  );
}


export default App;