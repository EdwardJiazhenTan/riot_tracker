import { useState } from 'react';
import { Home } from './page/Home.tsx';
import { MatchDetail } from './page/MatchDetail.tsx';

interface SearchState {
  gameName: string;
  tagLine: string;
  count: string;
}

export default function App() {
  const [currentView, setCurrentView] = useState<'home' | 'matchDetail'>('home');
  const [selectedMatch, setSelectedMatch] = useState<{ matchId: string; puuid: string } | null>(null);
  const [searchState, setSearchState] = useState<SearchState>({ gameName: '', tagLine: '', count: '' });

  const handleMatchClick = (matchId: string, puuid: string) => {
    setSelectedMatch({ matchId, puuid });
    setCurrentView('matchDetail');
  };

  const handleBackToHome = () => {
    setCurrentView('home');
    setSelectedMatch(null);
  };

  const handleSearchStateChange = (state: SearchState) => {
    setSearchState(state);
  };

  return (
    <div>
      {currentView === 'home' && (
        <Home
          onMatchClick={handleMatchClick}
          onSearchStateChange={handleSearchStateChange}
          initialSearchState={searchState}
        />
      )}
      {currentView === 'matchDetail' && selectedMatch && (
        <MatchDetail
          matchId={selectedMatch.matchId}
          puuid={selectedMatch.puuid}
          onBack={handleBackToHome}
        />
      )}
    </div>
  );
}
