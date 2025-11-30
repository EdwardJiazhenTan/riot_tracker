import { useState, useEffect } from 'react';
import { RecentMatches } from '../components/RecentMatches';

interface SearchState {
  gameName: string;
  tagLine: string;
  count: string;
}

interface HomeProps {
  onMatchClick: (matchId: string, puuid: string) => void;
  onSearchStateChange: (state: SearchState) => void;
  initialSearchState: SearchState;
}

export function Home({ onMatchClick, onSearchStateChange, initialSearchState }: HomeProps) {
  const [gameName, setGameName] = useState<string>(initialSearchState.gameName);
  const [tagLine, setTagLine] = useState(initialSearchState.tagLine);
  const [count, setCount] = useState(initialSearchState.count);
  const [showRecentMatches, setShowRecentMatches] = useState(
    !!(initialSearchState.gameName && initialSearchState.tagLine)
  );

  // Notify parent of state changes
  useEffect(() => {
    onSearchStateChange({ gameName, tagLine, count });
  }, [gameName, tagLine, count, onSearchStateChange]);

  const handleSubmit = () => {
    if (gameName && tagLine) {
      setShowRecentMatches(true);
    }
  }

  return (
    <div className=" m-4 justify-center font-mono">

      <h1 className="items-center text-center text-4xl">
        Get League stats & reports
      </h1>

      <div className="item-center flex justify-center-safe w-full mt-3">
        <input
          type='text'
          placeholder='summoner name'
          value={gameName}
          onChange={(e) => setGameName(e.target.value)}
          className="border border-black rounded-md p-1 m-2 w-50"
        />

        <input
          type='text'
          placeholder='#tag'
          value={tagLine}
          onChange={(e) => setTagLine(e.target.value)}
          className="border border-black rounded-md p-1 m-2 w-20"
        />

        <input
          type='number'
          placeholder='count'
          value={count}
          onChange={(e) => setCount(e.target.value)}
          className="w-20 border rounded-md border-black p-1 m-2"
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
          onMatchClick={onMatchClick}
        />
      </div>}

    </div>
  );
}
