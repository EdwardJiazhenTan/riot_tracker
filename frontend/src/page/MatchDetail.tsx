import { useState, useEffect } from 'react';
import { RadarChart, PolarGrid, PolarAngleAxis, PolarRadiusAxis, Radar, Legend, ResponsiveContainer } from 'recharts';
import ReactMarkdown from 'react-markdown';

interface PlayerRadarStats {
  puuid: string;
  summonerName: string;
  championName: string;
  lane: string;
  win: boolean;
  damage: number;
  damageTaken: number;
  farm: number;
  gold: number;
  vision: number;
  kda: number;
  totalDamageDealtToChampions: number;
  totalDamageTaken: number;
  totalMinionsKilled: number;
  neutralMinionsKilled: number;
  goldEarned: number;
  visionScore: number;
  wardsPlaced: number;
  wardsKilled: number;
  kills: number;
  deaths: number;
  assists: number;
}

interface RadarChartStats {
  matchId: string;
  playerStats: PlayerRadarStats;
  opponentStats: PlayerRadarStats;
}

interface MatchDetailProps {
  matchId: string;
  puuid: string;
  onBack?: () => void;
}

function getChampionAvatarUrl(championName: string): string {
  const DD_VERSION = '14.23.1';
  return `https://ddragon.leagueoflegends.com/cdn/${DD_VERSION}/img/champion/${championName}.png`;
}

interface GameReportComparison {
  provider: string;
  model: string;
  success: boolean;
  report: string;
  error?: string;
  responseTime: number;
  inputTokens?: number;
  outputTokens?: number;
  totalTokens?: number;
  timestamp: string;
}

export function MatchDetail({ matchId, puuid, onBack }: MatchDetailProps) {
  const [radarStats, setRadarStats] = useState<RadarChartStats | null>(null);
  const [error, setError] = useState<string>('');
  const [loading, setLoading] = useState(false);

  // AI Report states
  const [aiReport, setAiReport] = useState<string>('');
  const [aiReportLoading, setAiReportLoading] = useState(false);
  const [aiReportError, setAiReportError] = useState<string>('');

  // Advanced comparison states
  const [showAdvanced, setShowAdvanced] = useState(false);
  const [comparisons, setComparisons] = useState<GameReportComparison[]>([]);
  const [comparisonLoading, setComparisonLoading] = useState(false);
  const [comparisonError, setComparisonError] = useState<string>('');

  useEffect(() => {
    async function fetchRadarStats() {
      try {
        setLoading(true);
        setError('');

        const url = `http://localhost:8080/api/match/${matchId}/radar?puuid=${puuid}`;
        const response = await fetch(url);

        if (!response.ok) {
          setError('Error fetching match details');
          return;
        }

        const data: RadarChartStats = await response.json() as RadarChartStats;
        setRadarStats(data);
      } catch (error) {
        setError(`Error occurred: ${error}`);
      } finally {
        setLoading(false);
      }
    }

    fetchRadarStats();
  }, [matchId, puuid]);

  const handleGenerateReport = async () => {
    try {
      setAiReportLoading(true);
      setAiReportError('');
      setAiReport('');

      const url = `http://localhost:8080/api/game-tracker/report-by-match?matchId=${matchId}&puuid=${puuid}`;
      const response = await fetch(url);

      if (!response.ok) {
        throw new Error('Failed to generate report');
      }

      const report = await response.text();
      setAiReport(report);
    } catch (error) {
      setAiReportError(`Error: ${error}`);
    } finally {
      setAiReportLoading(false);
    }
  };

  const handleCompareReports = async () => {
    try {
      setComparisonLoading(true);
      setComparisonError('');
      setComparisons([]);

      const url = `http://localhost:8080/api/game-tracker/compare-by-match?matchId=${matchId}&puuid=${puuid}`;
      const response = await fetch(url);

      if (!response.ok) {
        throw new Error('Failed to compare reports');
      }

      const data: GameReportComparison[] = await response.json() as GameReportComparison[];
      setComparisons(data);
    } catch (error) {
      setComparisonError(`Error: ${error}`);
    } finally {
      setComparisonLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center p-10">
        <h3>Loading match details...</h3>
      </div>
    );
  }

  if (error) {
    return (
      <div className="p-4 bg-red-100 text-red-800 rounded-md m-4">
        {error}
      </div>
    );
  }

  if (!radarStats) {
    return (
      <div className="p-4">
        <p>No data available</p>
      </div>
    );
  }

  const { playerStats, opponentStats } = radarStats;

  // Prepare data for radar chart
  // Add a hidden reference point at 100 to prevent auto-scaling
  const chartData = [
    {
      stat: 'DMG/min',
      player: playerStats.damage,
      opponent: opponentStats.damage,
      reference: 100,
    },
    {
      stat: 'DMG Taken/min',
      player: playerStats.damageTaken,
      opponent: opponentStats.damageTaken,
      reference: 100,
    },
    {
      stat: 'CS/min',
      player: playerStats.farm,
      opponent: opponentStats.farm,
      reference: 100,
    },
    {
      stat: 'Gold/min',
      player: playerStats.gold,
      opponent: opponentStats.gold,
      reference: 100,
    },
    {
      stat: 'Wards/min',
      player: playerStats.vision,
      opponent: opponentStats.vision,
      reference: 100,
    },
    {
      stat: 'KDA',
      player: playerStats.kda,
      opponent: opponentStats.kda,
      reference: 100,
    },
  ];

  return (
    <div className="p-4 font-mono max-w-6xl mx-auto">
      {/* Back Button */}
      {onBack && (
        <button
          onClick={onBack}
          className="mb-4 border border-black px-3 py-1 rounded hover:bg-gray-100"
        >
          ← Back
        </button>
      )}

      {/* Header */}
      <h1 className="text-3xl text-center mb-6">Match Comparison</h1>

      {/* Player Comparison Header */}
      <div className="flex justify-around items-center mb-8">
        {/* Player */}
        <div className="text-center">
          <img
            src={getChampionAvatarUrl(playerStats.championName)}
            alt={playerStats.championName}
            className="w-24 h-24 rounded-full border-4 border-blue-500 mx-auto mb-2"
            onError={(e) => {
              e.currentTarget.src = 'https://via.placeholder.com/96';
            }}
          />
          <h2 className="text-xl font-bold">{playerStats.summonerName}</h2>
          <p className="text-gray-600">{playerStats.championName}</p>
          <p className={`font-bold ${playerStats.win ? 'text-green-600' : 'text-red-600'}`}>
            {playerStats.win ? 'Victory' : 'Defeat'}
          </p>
        </div>

        {/* VS */}
        <div className="text-4xl font-bold text-gray-400">VS</div>

        {/* Opponent */}
        <div className="text-center">
          <img
            src={getChampionAvatarUrl(opponentStats.championName)}
            alt={opponentStats.championName}
            className="w-24 h-24 rounded-full border-4 border-red-500 mx-auto mb-2"
            onError={(e) => {
              e.currentTarget.src = 'https://via.placeholder.com/96';
            }}
          />
          <h2 className="text-xl font-bold">{opponentStats.summonerName}</h2>
          <p className="text-gray-600">{opponentStats.championName}</p>
          <p className={`font-bold ${opponentStats.win ? 'text-green-600' : 'text-red-600'}`}>
            {opponentStats.win ? 'Victory' : 'Defeat'}
          </p>
        </div>
      </div>

      {/* Radar Chart */}
      <div className="bg-white border border-gray-300 rounded-lg p-6 mb-6">
        <h3 className="text-xl text-center mb-4">Performance Comparison vs Gold Rank</h3>
        <p className="text-sm text-center text-gray-600 mb-4">
          100 = Gold rank average for role | All stats are per-minute
        </p>
        <ResponsiveContainer width="100%" height={400}>
          <RadarChart data={chartData}>
            <PolarGrid />
            <PolarAngleAxis dataKey="stat" />
            <PolarRadiusAxis angle={90} domain={[0, 100]} tick={false} />
            {/* Hidden reference radar to prevent auto-scaling */}
            <Radar
              dataKey="reference"
              stroke="transparent"
              fill="transparent"
              dot={false}
            />
            <Radar
              name={playerStats.summonerName}
              dataKey="player"
              stroke="#3b82f6"
              fill="#3b82f6"
              fillOpacity={0.6}
            />
            <Radar
              name={opponentStats.summonerName}
              dataKey="opponent"
              stroke="#ef4444"
              fill="#ef4444"
              fillOpacity={0.6}
            />
            <Legend />
          </RadarChart>
        </ResponsiveContainer>
      </div>

      {/* Detailed Stats */}
      <div className="grid grid-cols-2 gap-4">
        {/* Player Stats */}
        <div className="border border-blue-500 rounded-lg p-4">
          <h3 className="text-lg font-bold mb-3 text-blue-600">
            {playerStats.summonerName}
          </h3>
          <div className="space-y-2 text-sm">
            <div className="flex justify-between">
              <span>KDA:</span>
              <span className="font-bold">{playerStats.kills}/{playerStats.deaths}/{playerStats.assists}</span>
            </div>
            <div className="flex justify-between">
              <span>Damage:</span>
              <span>{playerStats.totalDamageDealtToChampions.toLocaleString()}</span>
            </div>
            <div className="flex justify-between">
              <span>Damage Taken:</span>
              <span>{playerStats.totalDamageTaken.toLocaleString()}</span>
            </div>
            <div className="flex justify-between">
              <span>Farm:</span>
              <span>{playerStats.totalMinionsKilled + playerStats.neutralMinionsKilled} CS</span>
            </div>
            <div className="flex justify-between">
              <span>Gold:</span>
              <span>{playerStats.goldEarned.toLocaleString()}</span>
            </div>
            <div className="flex justify-between">
              <span>Vision Score:</span>
              <span>{playerStats.visionScore}</span>
            </div>
            <div className="flex justify-between">
              <span>Wards Placed:</span>
              <span>{playerStats.wardsPlaced}</span>
            </div>
            <div className="flex justify-between">
              <span>Wards Killed:</span>
              <span>{playerStats.wardsKilled}</span>
            </div>
          </div>
        </div>

        {/* Opponent Stats */}
        <div className="border border-red-500 rounded-lg p-4">
          <h3 className="text-lg font-bold mb-3 text-red-600">
            {opponentStats.summonerName}
          </h3>
          <div className="space-y-2 text-sm">
            <div className="flex justify-between">
              <span>KDA:</span>
              <span className="font-bold">{opponentStats.kills}/{opponentStats.deaths}/{opponentStats.assists}</span>
            </div>
            <div className="flex justify-between">
              <span>Damage:</span>
              <span>{opponentStats.totalDamageDealtToChampions.toLocaleString()}</span>
            </div>
            <div className="flex justify-between">
              <span>Damage Taken:</span>
              <span>{opponentStats.totalDamageTaken.toLocaleString()}</span>
            </div>
            <div className="flex justify-between">
              <span>Farm:</span>
              <span>{opponentStats.totalMinionsKilled + opponentStats.neutralMinionsKilled} CS</span>
            </div>
            <div className="flex justify-between">
              <span>Gold:</span>
              <span>{opponentStats.goldEarned.toLocaleString()}</span>
            </div>
            <div className="flex justify-between">
              <span>Vision Score:</span>
              <span>{opponentStats.visionScore}</span>
            </div>
            <div className="flex justify-between">
              <span>Wards Placed:</span>
              <span>{opponentStats.wardsPlaced}</span>
            </div>
            <div className="flex justify-between">
              <span>Wards Killed:</span>
              <span>{opponentStats.wardsKilled}</span>
            </div>
          </div>
        </div>
      </div>

      {/* AI Report Section */}
      <div className="mt-6 border border-gray-300 rounded-lg p-4">
        <div className="flex justify-between items-center mb-4">
          <h3 className="text-xl font-bold">AI Analysis</h3>
          <div className="flex gap-2">
            <button
              onClick={handleGenerateReport}
              disabled={aiReportLoading}
              className="border border-black px-4 py-2 rounded hover:bg-gray-100 disabled:bg-gray-200 disabled:cursor-not-allowed"
            >
              {aiReportLoading ? 'Generating...' : 'Generate AI Report'}
            </button>
            <button
              onClick={() => setShowAdvanced(!showAdvanced)}
              className="border border-blue-500 text-blue-500 px-4 py-2 rounded hover:bg-blue-50"
            >
              {showAdvanced ? 'Hide Advanced' : 'Advanced'}
            </button>
          </div>
        </div>

        {/* AI Report Display */}
        {aiReportError && (
          <div className="p-4 bg-red-100 text-red-800 rounded-md mb-4">
            {aiReportError}
          </div>
        )}

        {aiReport && (
          <div className="bg-gray-50 p-4 rounded-md border border-gray-200 prose prose-sm max-w-none">
            <ReactMarkdown>{aiReport}</ReactMarkdown>
          </div>
        )}

        {/* Advanced Comparison Section */}
        {showAdvanced && (
          <div className="mt-4 border-t pt-4">
            <div className="mb-4">
              <button
                onClick={handleCompareReports}
                disabled={comparisonLoading}
                className="border border-purple-500 text-purple-500 px-4 py-2 rounded hover:bg-purple-50 disabled:bg-gray-200 disabled:cursor-not-allowed"
              >
                {comparisonLoading ? 'Comparing Models...' : 'Compare AI Models (Claude vs GPT)'}
              </button>
            </div>

            {comparisonError && (
              <div className="p-4 bg-red-100 text-red-800 rounded-md mb-4">
                {comparisonError}
              </div>
            )}

            {comparisons.length > 0 && (
              <div className="space-y-4">
                {comparisons.map((comparison, index) => (
                  <div key={index} className="border border-gray-300 rounded-lg p-4">
                    <div className="flex justify-between items-start mb-3">
                      <div>
                        <h4 className="text-lg font-bold">{comparison.provider}</h4>
                        <p className="text-sm text-gray-600">{comparison.model}</p>
                      </div>
                      <div className="text-right text-sm">
                        <div className="font-bold text-green-600">
                          {comparison.responseTime.toFixed(2)}s
                        </div>
                        {comparison.totalTokens && (
                          <div className="text-gray-600">
                            {comparison.totalTokens.toLocaleString()} tokens
                          </div>
                        )}
                      </div>
                    </div>

                    {comparison.success ? (
                      <div className="bg-gray-50 p-3 rounded border border-gray-200 prose prose-sm max-w-none">
                        <ReactMarkdown>{comparison.report}</ReactMarkdown>
                      </div>
                    ) : (
                      <div className="bg-red-50 p-3 rounded border border-red-200 text-red-800">
                        Error: {comparison.error}
                      </div>
                    )}

                    {comparison.inputTokens && comparison.outputTokens && (
                      <div className="mt-2 text-xs text-gray-500 flex gap-4">
                        <span>Input: {comparison.inputTokens.toLocaleString()}</span>
                        <span>Output: {comparison.outputTokens.toLocaleString()}</span>
                      </div>
                    )}
                  </div>
                ))}
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
}
