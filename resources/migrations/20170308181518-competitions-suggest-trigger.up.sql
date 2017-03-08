--Add suggest column to competition
ALTER TABLE competitions
  ADD COLUMN suggest TEXT;

--Function to update the competition suggestion field
CREATE OR REPLACE FUNCTION update_competitions()
  RETURNS TRIGGER AS $$
BEGIN
  NEW.suggest := NEW.id || ' ' || NEW.description || ' ' || NEW.start_date || ' ' || NEW.end_date;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

--Trigger to update the user suggestion field
CREATE TRIGGER update_competitions_trg
BEFORE INSERT OR UPDATE ON competitions
FOR EACH ROW EXECUTE PROCEDURE update_competitions();

--Run trigger for all existing rows
UPDATE competitions
SET description = description;
