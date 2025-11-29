import React, { useEffect, useState } from 'react';

export function UserInfo() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [userStats, setUserStats] = useState('');
  const [summonerName, setSummonerName] = useState('');
  const [summonerTag, setSummonerTag] = useState('');

  async function handleSubmit() {
    setLoading(true);
    console.log(' handle submit');
    try {
      const response = await fetch(`http://localhost:8080/api/summoner/${summonerName}/${summonerTag}`);
      if (!response.ok) {
        setError('error when fetching summoner datas');
        return;
      }
      const userData = await response.text();
      setUserStats(userData);
    } catch (error) {
      setError('failed to fetch data');
    } finally {
      setLoading(false);
    }
  }


  return (
    <div>
      <input
        type="text"
        placeholder="summoner name"
        value={summonerName}
        onChange={(e) => setSummonerName(e.target.value)}
      />

      <input
        type="text"
        placeholder="summoner Tag"
        value={summonerTag}
        onChange={(e) => setSummonerTag(e.target.value)}
      />

      <button
        onClick={(e) => handleSubmit()}
      >
        submit
      </button>

      {error && <p> {error}</p>}
      {loading && <p> loading stats </p>}

      {
        userStats &&
        <div>
          {userStats}
        </div>
      }

    </div>
  );
}
