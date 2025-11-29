import React, { useState } from 'react';
import { RecentMatches } from '../components/RecentMatches';

export function Home() {
  const [gameName, setGameName] = useState<string>('');
  const [tagLine, setTagLine] = useState('');
  const [count, setCount] = useState('');
  const [showRecentMatches, setShowRecentMatches] = useState(false);

  const handleSubmit = () => {
    if (gameName && tagLine) {
      setShowRecentMatches(true);
    }
  }

  return (
    <div className=" m-4 justify-center font-mono">

      <h1 className="items-center text-center text-2xl">
        Get your recent League stats & reports
      </h1>

      <div className="flex flex-col items-center w-full mt-3">
        <input
          type='text'
          placeholder='summoner name'
          value={gameName}
          onChange={(e) => setGameName(e.target.value)}
          className="border text-left border-black p-1 m-2"
        />

        <input
          type='text'
          placeholder='game tagline'
          value={tagLine}
          onChange={(e) => setTagLine(e.target.value)}
          className="border text-left border-black p-1 m-2"
        />

        <input
          type='number'
          placeholder='count of game reports'
          value={count}
          onChange={(e) => setCount(e.target.value)}
          className="border text-left border-black p-1 m-2"
        />

        <button
          onClick={handleSubmit}
          className="border text-left border-black p-1 m-2"
        >
          submit
        </button>
      </div>

      {showRecentMatches && <div>
        <RecentMatches
          gameName={gameName}
          tagLine={tagLine}
          count={parseInt(count)}
        />
      </div>}

    </div>
  );
}
