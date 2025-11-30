import { useState, useEffect } from 'react';

interface MatchSummary {
  matchId: string;
  championName: string;
  kills: number;
  deaths: number;
  assists: number;
  win: boolean;
  gameDuration: number;
  gameCreation: number;
}

interface RecentMatchesProps {
  gameName: string;
  tagLine: string;
  count: number;
  onMatchClick: (matchId: string, puuid: string) => void;
}

// Helper function to calculate KDA ratio
function calculateKDA(kills: number, deaths: number, assists: number): string {
  if (deaths === 0) return 'Perfect';
  return ((kills + assists) / deaths).toFixed(2);
}

// Helper function to get champion avatar URL
function getChampionAvatarUrl(championName: string): string {
  const DD_VERSION = '14.23.1';
  return `https://ddragon.leagueoflegends.com/cdn/${DD_VERSION}/img/champion/${championName}.png`;
}

export function RecentMatches({ gameName, tagLine, count, onMatchClick }: RecentMatchesProps) {
  const [recentMatches, setRecentMatches] = useState<MatchSummary[]>([]);
  const [error, setError] = useState<string>('');
  const [loading, setLoading] = useState(false);
  const [puuid, setPuuid] = useState<string>('');

  // Fetch PUUID and matches when component mounts or props change
  useEffect(() => {
    async function fetchData() {
      try {
        setLoading(true);
        setError('');

        // First, get the PUUID
        const puuidUrl = `http://localhost:8080/api/summoner/${gameName}/${tagLine}/puuid`;
        const puuidResponse = await fetch(puuidUrl);

        if (!puuidResponse.ok) {
          setError('Error fetching player info');
          return;
        }

        const puuidData = await puuidResponse.json();
        const fetchedPuuid = puuidData.puuid;
        setPuuid(fetchedPuuid);

        // Then fetch matches
        const url = `http://localhost:8080/api/matches?gameName=${gameName}&tagLine=${tagLine}&count=${count}`;
        const response = await fetch(url);

        if (!response.ok) {
          setError('Error fetching recent matches');
          return;
        }

        const matches: MatchSummary[] = await response.json() as MatchSummary[];
        setRecentMatches(matches);
      } catch (error) {
        setError(`Error occurred: ${error}`);
      } finally {
        setLoading(false);
      }
    }

    fetchData();
  }, [gameName, tagLine, count]); // Re-fetch when these change

  return (
    <div style={{ maxWidth: '800px', margin: '0 auto', padding: '20px' }}>
      <h1 style={{ marginBottom: '20px' }}>Recent Matches</h1>

      {error && (
        <div style={{
          padding: '16px',
          backgroundColor: '#f8d7da',
          color: '#721c24',
          borderRadius: '4px',
          marginBottom: '20px'
        }}>
          {error}
        </div>
      )}

      {loading && (
        <div style={{ textAlign: 'center', padding: '40px' }}>
          <h3>Loading matches...</h3>
        </div>
      )}

      {!loading && !error && recentMatches.length === 0 && (
        <p style={{ textAlign: 'center', color: '#666' }}>No matches found</p>
      )}

      {/* Render match cards */}
      <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        {recentMatches.map((match) => {
          const kda = calculateKDA(match.kills, match.deaths, match.assists);
          const duration = `${Math.floor(match.gameDuration / 60)}m ${match.gameDuration % 60}s`;
          const playedDate = new Date(match.gameCreation).toLocaleDateString();

          return (
            <div
              key={match.matchId}
              onClick={() => onMatchClick(match.matchId, puuid)}
              className={`flex items-center p-2 border rounded-2xl cursor-pointer ${match.win ? " bg-[#d4edda] hover:bg-[#d4edda]/50 active:bg-[#d4edda]/75 " : "bg-[#f8d7da] hover:bg-[#f8d7da]/50 active:bg-[#f8d7da]/75"}`}
            >
              {/* Champion Avatar */}
              <div style={{ marginRight: '16px', flexShrink: 0 }}>
                <img
                  src={getChampionAvatarUrl(match.championName)}
                  alt={match.championName}
                  width={64}
                  height={64}
                  className="rounded-2xl border"
                  onError={(e) => {
                    // Fallback if image fails to load
                    e.currentTarget.src = 'https://via.placeholder.com/64';
                  }}
                />
                <h3 style={{ margin: '0 0 8px 0', fontSize: '18px' }}>
                  {match.championName}
                </h3>
              </div>

              {/* Match Info */}
              <div className="flex-1">
                <div className="flex place-content-between items-center">
                  <div>

                    <p className={`m-0 font-bold ${match.win ? "text-[#155724]" : "text-[#721c24]"}`}>
                      {match.win ? '✓ Victory' : '✗ Defeat'}
                    </p>
                  </div>

                  <p className="text-xl">
                    {match.kills}/{match.deaths}/{match.assists}
                  </p>

                  <div className="text-center">
                    <p style={{ margin: '0', color: '#666' }}>
                      <strong>Ratio:</strong> {kda}
                    </p>
                  </div>
                </div>

                <div style={{
                  marginTop: '12px',
                  paddingTop: '12px',
                  borderTop: '1px solid rgba(0,0,0,0.1)',
                  display: 'flex',
                  justifyContent: 'space-between',
                  fontSize: '14px',
                  color: '#666'
                }}>
                  <span>{duration}</span>
                  <span>{playedDate}</span>
                  <span style={{ fontSize: '12px', color: '#999' }}>{match.matchId}</span>
                </div>
              </div>
            </div>
          );
        })}
      </div>
    </div >
  );
}
