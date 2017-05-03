ALTER TABLE results
  ADD COLUMN shots_mirror TEXT;

CREATE OR REPLACE FUNCTION update_results()
  RETURNS TRIGGER AS $$
BEGIN
  NEW.shots_mirror := reverse(NEW.shots);
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_results_trg
BEFORE INSERT OR UPDATE ON results
FOR EACH ROW EXECUTE PROCEDURE update_results();
